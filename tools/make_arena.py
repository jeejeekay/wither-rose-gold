#!/usr/bin/env python3
"""Writes an all-air structure .nbt (the GameTest 'arena'): python3 make_arena.py out.nbt X Y Z"""
import gzip, struct, sys
out, X, Y, Z = sys.argv[1], *map(int, sys.argv[2:5])
def s(v): b = v.encode(); return struct.pack(">H", len(b)) + b
def tag(t, name, payload): return bytes([t]) + s(name) + payload
def i(v): return struct.pack(">i", v)
def lst(t, items): return bytes([t]) + i(len(items)) + b"".join(items)
def comp(fields): return b"".join(fields) + b"\x00"
blocks = [comp([tag(9, "pos", lst(3, [i(x), i(y), i(z)])), tag(3, "state", i(0))])
          for x in range(X) for y in range(Y) for z in range(Z)]
root = tag(10, "", comp([
    tag(3, "DataVersion", i(3953)),   # 1.21; newer versions upgrade it automatically
    tag(9, "size", lst(3, [i(X), i(Y), i(Z)])),
    tag(9, "palette", lst(10, [comp([tag(8, "Name", s("minecraft:air"))])])),
    tag(9, "blocks", lst(10, blocks)),
    tag(9, "entities", lst(10, [])),
]))
with open(out, "wb") as f: f.write(gzip.compress(root))
print(out, len(blocks), "blocks")
