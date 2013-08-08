data Head = N | E | S | W deriving (Show, Read, Enum, Bounded)
data Rover = Rover Int Int Head deriving (Show, Read)
data Command = L | R | M deriving (Show, Read)
data Delta = Delta Int Int

delta N = Delta   0    1
delta E = Delta   1    0
delta S = Delta (-1)   0
delta W = Delta   0  (-1)

turn n e = toEnum (add (fromEnum (maxBound `asTypeOf` e) + 1) (fromEnum e) n)
    where add mod x y = (x + y + mod) `rem` mod
move (Rover x y h) (Delta dx dy) = Rover (x + dx) (y + dy) h

act   (Rover x y h) L = Rover x y (turn (-1) h)
act   (Rover x y h) R = Rover x y (turn   1  h)
act r@(Rover x y h) M = move r $ delta h

travel' rover commands = foldl act rover commands

toList x = [x]
readRover    input = read ("Rover " ++ input)
readCommands input = map (read . toList) input

travel rover commands = travel' (readRover rover) (readCommands commands)

work = do
  rover    <- getLine
  commands <- getLine
  putStrLn $ show $ travel rover commands
  work

main = do
  skip <- getLine
  work
