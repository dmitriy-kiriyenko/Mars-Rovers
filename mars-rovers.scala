object Direction extends Enumeration {

  type Direction = Value
  val E, S, W, N = Value

  def leftFrom(d: Direction) = shift(d, -1)

  def rightFrom(d: Direction) = shift(d, +1)

  private def shift(d: Direction, dx: Int) =
    values.toList((d.id + dx + values.size) % values.size)
}

case class Plateau(width: Int, height: Int) {
  def contains(point: Point) =
    point.x >= 0 && point.y >= 0 &&
      point.x < width && point.y < height
}

case class Point(x: Int, y: Int) {
  def move(d: Direction.Direction): Point = d match {
    case Direction.N => Point(x, y + 1)
    case Direction.W => Point(x - 1, y)
    case Direction.E => Point(x + 1, y)
    case Direction.S => Point(x, y - 1)
  }
}

case class Rover(plateau: Plateau, loc: Point, direction: Direction.Direction) {

  def move(moves: String): Rover = moves.foldLeft(this)(_ action _)

  def action(action: Char): Rover = action match {
    case 'L' => left
    case 'R' => right
    case 'M' => move
  }

  def left: Rover = Rover(plateau, loc, Direction.leftFrom(direction))

  def right: Rover = Rover(plateau, loc, Direction.rightFrom(direction))

  def move: Rover = {
    val newLocation = Option(loc.move(direction))
      .filter(plateau.contains)
      .getOrElse(loc)
    Rover(plateau, newLocation, direction)
  }
}

object Main extends App {
  val lines = scala.io.Source.fromFile(args(0)).getLines()
  val Array(width, height) = lines.next().split(" ").map(_.toInt)
  val plateau = Plateau(width, height)
  val results = for {
    Seq(coordinates, moves) <- lines.sliding(2, 2)
    Array(x, y, direction) = coordinates.split(" ")
    rover = Rover(plateau, Point(x.toInt, y.toInt), Direction.withName(direction))
  } yield rover.move(moves)

  results.foreach(println)
}
