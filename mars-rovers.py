class RectangularPlateau(object):
    def __init__(self, line):
        self.width, self.height = map(int, line.strip().split()[:2])

    def contains(self, x, y):
        return x >= 0 and x <= self.width and y >= 0 and y <= self.height


class Rover(object):
    def __init__(self, line, plateau):
        parts = line.split()
        self.x = int(parts[0].strip())
        self.y = int(parts[1].strip())
        self.orientation = 'NESW'.find(parts[2].strip()[0])
        self.plateau = plateau

    def perform_command(self, command):
        if command == 'M':
            self.move(*[
                (0, 1),
                (1, 0),
                (0, -1),
                (-1, 0),
            ][self.orientation])
        elif command == 'L':
            self.orientation = (self.orientation + 3) % 4
        elif command == 'R':
            self.orientation = (self.orientation + 1) % 4

    def move(self, x, y):
        next_x = self.x + x
        next_y = self.y + y
        if self.plateau.contains(next_x, next_y):
            self.x = next_x
            self.y = next_y

    def perform(self, program):
        for command in program:
            self.perform_command(command)

    def __str__(self):
        return '%d %d %s' % (self.x, self.y, 'NESW'[self.orientation])


def read_rover(stream):
    while True:
        line = stream.readline().strip()
        if line:
            rover = Rover(line, plateau)
            program = stream.readline().strip()
            yield rover, program
        else:
            break

if __name__ == '__main__':
    with open('input.txt') as f:
        plateau = RectangularPlateau(f.readline())

        for rover, program in read_rover(f):
            rover.perform(program)
            print rover

