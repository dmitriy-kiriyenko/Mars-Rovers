data Head = N | E | S | W deriving (Show, Read, Enum, Bounded)
data Rover = Rover Int Int Head deriving (Show, Read)
data Command = L | R | M deriving (Show, Read)
data Delta = Delta Int Int

delta N = Delta   0    1
delta E = Delta   1    0
delta S = Delta   0  (-1)
delta W = Delta (-1)   0

turn n e = toEnum (add (fromEnum (maxBound `asTypeOf` e) + 1) (fromEnum e) n)
    where add mod x y = (x + y + mod) `rem` mod
move (Rover x y h) (Delta dx dy) = Rover (x + dx) (y + dy) h

act L   (Rover x y h) = Rover x y (turn (-1) h)
act R   (Rover x y h) = Rover x y (turn   1  h)
act M r@(Rover x y h) = move r $ delta h

action commands = foldr1 (flip (.)) $ map act commands

toList x = [x]
readRover    input = read ("Rover " ++ input)
readCommands input = map (read . toList) input

travel rover commands = action (readCommands commands) (readRover rover)

work = map (show . (uncurry travel))

toPairs [] = []
toPairs (p:r:rest) = (p, r) : toPairs rest

main = interact (unlines . work . toPairs . tail . lines)
