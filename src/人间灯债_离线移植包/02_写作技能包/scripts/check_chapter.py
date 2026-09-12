# -*- coding: utf-8 -*-
"""网文章节体检脚本（每章交付前跑一遍）

用法：
    python check_chapter.py 第001章_xxx.md [第002章_yyy.md ...]
    python check_chapter.py --dir 02_正文
    python check_chapter.py --dir 02_正文 --special   # 特批档：字数上限放宽到 3500

覆盖检查项：
    1. 字数与段落地模三条线（均段 >=25 字 / <=12 字短段 <=25% / 连续单句成段 <=3 行）
    2. 标点规范（破折号、直角引号、引号配对）
    3. 否定式反应（没人笑 / 鸦雀无声 / 一片寂静 ...）
    4. 称谓雷区（「他妈 + 名词/量词」这类会被读成脏话的结构）
    5. 喻体复用（像 / 似的 / 如同 / 仿佛 之后的喻体频次，>=2 次报警）
    6. 对话占比
    7. 字数分档（常规 2000-3000 / 特批 3000-3500 / 4000 红线）
    8. 生僻字扫描（书面文学字命中即改，口径见 craft.md 五点八第 3 条）

退出码：0 = 全部通过；1 = 有项目需要人工确认。
"""
import collections
import io
import os
import re
import sys

if sys.stdout.encoding and sys.stdout.encoding.lower() != 'utf-8':
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

# ---- 检查用正则 ----------------------------------------------------------
NEG_RE = re.compile(r'没人[\u4e00-\u9fa5]{0,3}|谁也没[\u4e00-\u9fa5]{0,3}|鸦雀无声|一片寂静|全场')
BAD_CALL_RE = re.compile(r'他妈(?!妈)|你妈(?!妈)|妈的|尼玛|傻逼')
SIMILE_RE = re.compile(r'(?:像|似的|如同|仿佛)([\u4e00-\u9fa5]{2,6})')

MIN_CHARS, SOFT_MAX, HARD_MAX, FLOOR = 2000, 3000, 3500, 1800
AVG_MIN, SHORT_MAX_RATIO, RUN_MAX = 25.0, 25.0, 3

# 生僻字池：书面文学字，口语里不用，正文命中即改（北方口语常用字如 攥/茬/蔫/撂/搪 不入池）。
# 判定口径见 craft.md 五点八第 3 条。可按需扩充；若某字确属反复使用的设定词，从池中移除。
RARE_CHARS = '洇拃氤氲皴皑翕阖囿彳亍旖旎醭饧齁齉簌觳饕餮罅龌龊龃龉蹀躞愀魇魉氽焯粜籴甏潲泅苶搋欻扽薅窸窣嚅嗫戥笸箩趔趄瘆怆恸'


def load_paras(path):
    with open(path, encoding='utf-8') as f:
        raw = f.read()
    paras = [l.strip() for l in raw.split('\n') if l.strip() and not l.strip().startswith('#') and not l.strip().startswith('---')]
    return raw, paras


def n_sent(p):
    return len([s for s in re.split(r'[。！？…]', p) if s.strip()])


def check(path, soft_max=SOFT_MAX):
    raw, paras = load_paras(path)
    text = ''.join(paras)
    total = len(text)
    n = len(paras)
    avg = total / n if n else 0
    shorts = [p for p in paras if len(p) <= 12]
    short_ratio = len(shorts) / n * 100 if n else 0

    single = [n_sent(p) == 1 for p in paras]
    run = best = 0
    for s in single:
        run = run + 1 if s else 0
        best = max(best, run)

    dash = text.count('——')
    corner = text.count('「') + text.count('」')
    lq, rq = text.count('“'), text.count('”')
    dialog = sum(len(m) for m in re.findall(r'“([^”]*)”', text))
    dlg_ratio = dialog / total * 100 if total else 0

    problems = []

    # 1 字数
    if total > HARD_MAX:
        problems.append(f'字数 {total} 超 4000 绝对红线')
    elif total > soft_max:
        problems.append(f'字数 {total} 超 {soft_max}（仅情感高潮／单元开篇／单元收束章可用特批档至 3500，须在进度表注明理由）')
    elif total < FLOOR:
        problems.append(f'字数 {total} 低于 1800 下限')

    # 2 段落地模
    if avg < AVG_MIN:
        problems.append(f'均段 {avg:.1f} 字 < 25（段落过碎，按 craft.md 五节「完稿章节回修四步法」合并）')
    if short_ratio > SHORT_MAX_RATIO:
        problems.append(f'短段占比 {short_ratio:.1f}% > 25%（{len(shorts)} 处 <=12 字段落）')
    if best > RUN_MAX:
        problems.append(f'连续单句成段 {best} 行 > 3 行')

    # 3 标点
    if dash:
        problems.append(f'破折号 {dash} 处（用户偏好：正文避免破折号）')
    if corner:
        problems.append(f'直角引号 {corner} 处（对话一律用中文双引号）')
    if lq != rq:
        problems.append(f'引号不配对：左 {lq} / 右 {rq}')

    # 4 否定式反应
    negs = [m.group(0) for m in NEG_RE.finditer(text)]
    neg_hits = []
    for m in NEG_RE.finditer(text):
        ctx = text[max(0, m.start() - 12): m.end() + 12]
        neg_hits.append(ctx)

    # 5 称谓雷区
    calls = [(m.group(0), text[max(0, m.start() - 10): m.end() + 12]) for m in BAD_CALL_RE.finditer(text)]

    # 6 喻体复用
    sims = collections.Counter(m.group(1) for m in SIMILE_RE.finditer(text))
    dup = {k: v for k, v in sims.items() if v >= 2}

    # 7 生僻字（craft.md 五点八第 3 条）
    rare_hits = []
    for ch in RARE_CHARS:
        idx = text.find(ch)
        while idx != -1:
            rare_hits.append((ch, text[max(0, idx - 10): idx + 12]))
            idx = text.find(ch, idx + 1)

    # ---- 输出 ------------------------------------------------------------
    print(f'===== {os.path.basename(path)} =====')
    print(f'  字数 {total} | 段数 {n} | 均段 {avg:.1f} 字 | 短段 {len(shorts)} 处 ({short_ratio:.1f}%) | 连续单句最长 {best} 行')
    print(f'  破折号 {dash} | 直角引号 {corner} | 引号 {"配对 OK" if lq == rq else f"不配对 {lq}/{rq}"} | 对话占比 {dlg_ratio:.0f}%')
    if sims:
        print(f'  喻体共 {sum(sims.values())} 处，重复喻体 {len(dup)} 个' + (f'：{dup}' if dup else ''))

    if neg_hits:
        print(f'  [需人工判] 否定式反应候选 {len(neg_hits)} 处（对话里的事实现象不算违规）：')
        for c in neg_hits:
            print(f'     …{c}…')
    if calls:
        print(f'  [!] 称谓雷区 {len(calls)} 处（第二人称当面指称可保留）：')
        for k, c in calls:
            print(f'     {k} → …{c}…')

    if problems:
        print('  [x] 未通过：')
        for p in problems:
            print(f'     - {p}')
    if rare_hits:
        print(f'  [x] 生僻字 {len(rare_hits)} 处（换常见写法，见 craft.md 五点八第 3 条）：')
        for ch, c in rare_hits:
            print(f'     「{ch}」→ …{c}…')
    print()
    ok = not problems and not rare_hits
    return ok


def main():
    args = sys.argv[1:]
    special = '--special' in args
    args = [a for a in args if a != '--special']
    files = []
    if args and args[0] == '--dir':
        d = args[1]
        files = [os.path.join(d, f) for f in sorted(os.listdir(d)) if f.endswith('.md')]
    else:
        files = args
    if not files:
        print(__doc__)
        return 1
    soft = HARD_MAX if special else SOFT_MAX
    ok = True
    for f in files:
        ok = check(f, soft) and ok
    print('全部通过' if ok else '存在需处理项，见上')
    return 0 if ok else 1


if __name__ == '__main__':
    sys.exit(main())
