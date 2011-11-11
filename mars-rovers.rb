#!/usr/bin/ruby

DIRS = "NESW"
DIR_SHIFTS = [[0, 1], [1, 0], [0, -1], [-1, 0]]
DIR_COUNT = DIRS.size
OPS = {:R => lambda { |x, y, dir| [x, y, (dir + 1) % DIR_COUNT] },
       :L => lambda { |x, y, dir| [x, y, (dir - 1 + DIR_COUNT) % DIR_COUNT] },
       :M => lambda { |x, y, dir| create_rover(x + DIR_SHIFTS[dir][0], y + DIR_SHIFTS[dir][1], dir) } }

X_MIN, Y_MIN = 0, 0

def ensure_between a, min, max
    [max, [a, min].max].min
end

def create_rover x, y, dir
    [ensure_between(x, X_MIN, X_MAX), ensure_between(y, Y_MIN, Y_MAX), dir]
end

def read_pos pos
    pos_arr = pos.split(" ")
    create_rover pos_arr[0].to_i, pos_arr[1].to_i, DIRS.index(pos_arr[2])
end

def process pos, instrs
    instrs.chars.map(&:to_sym).inject(read_pos pos) { |r, i| OPS[i].call(*r) }
end

X_MAX, Y_MAX = ARGF.readline.split(" ").map(&:to_i)

ARGF.each_slice(2) do |pos, instrs|
    rover = process *[pos, instrs].map { |s| s.chomp.upcase }
    puts [rover[0].to_s, rover[1].to_s, DIRS[rover[2]]].join(" ")
end
