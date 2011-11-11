#!/usr/bin/ruby

DIRS = {:N => Complex(0, 1), :E => Complex(1, 0), :S => Complex(0, -1), :W => Complex(-1, 0)}
OPS = {:R => lambda { |pos, dir| [pos, dir*(-Complex::I)] },
       :L => lambda { |pos, dir| [pos, dir*Complex::I] },
       :M => lambda { |pos, dir| create_rover(pos + dir, dir) } }

X_MIN, Y_MIN = 0, 0

def ensure_between a, min, max
    [max, [a, min].max].min
end

def create_rover pos, dir
    [Complex(ensure_between(pos.real, X_MIN, X_MAX), ensure_between(pos.imag, Y_MIN, Y_MAX)), dir]
end

def read_pos pos
    pos_arr = pos.split(" ")
    create_rover(Complex(pos_arr[0].to_i, pos_arr[1].to_i), DIRS[pos_arr[2].to_sym])
end

def process pos, instrs
    instrs.chars.map(&:to_sym).inject(read_pos pos) { |r, i| OPS[i].call(*r) }
end

X_MAX, Y_MAX = ARGF.readline.split(" ").map(&:to_i)

ARGF.each_slice(2) do |pos, instrs|
    rover = process *[pos, instrs].map {|s| s.chomp.upcase}
    puts [rover[0].real.to_s, rover[0].imag.to_s, DIRS.invert[rover[1]]].join(" ")
end
