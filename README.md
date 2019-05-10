# Damysos

Damysos is an experiment stemming from the idea that tries could be used to store points in a
n-dimensional space in a way that would allow for fast querying of "neighboring" points.

In order to achieve this, we first have to turn each coordinate or every point into a base 4
number such that the more spacial proximity between two points, the more characters their
transformed coordinates share, and this, going from left to right.

For example, if point A's encoded abscissa coordinate is "111", point B's is "112" and point C's is
"100", we can tell that point A is closer to point B than it is of point C and point C is closer to
point A than it is of point B (along the aformentionned abscissa).

This way, in order to get the neighboring points of a GPS coordinate, we only have to compute the
"trie path" for those coordinates, and descend the trie at the desired depth (the level of
precision, or "zoom"). Then, we take all the leaves below that point.

What's interesting in this approach, in my opinion, resides in the fact that nowhere in the code we
are actually commparing GPS coordinates, calculating distances etc. The data structure itself, in
this case a Trie, _is_ the logic.

From early testing, on a _Intel Core i7-7700HQ @ 2.80GHz_ CPU and on a dataset of 128769 points, it
finds all the neighboring points of a given GPS coordinate in under 400μs.

The speed of that search, however, depends on the level of precision (or "zoom") you want to
achieve.  Although it may appear counter intuitive, a lower precision actually means a longer query
time. This is because, if we have a GeoTrie 10 levels deeps and we ask for a precision of 5, then
we'll descend 5 levels of the trie and then recursively explore all the branches from that point to
get all the points below it.
Hence, the lower the precision, the less we descend the trie before we start exploring all of its
sub-tries, so the more branches we'll have to explore from that point.

## Usage

First, create a Damysos instance. Then, feed it multiple `PointOfInterst` to add data to it:
```scala
import damysos.Damysos

val damysos = Damysos()
val paris = PointOfInterst("Paris" Coordinates(43.2D, -80.38333D))
val toulouse = PointOfInterst("Toulouse" Coordinates(43.60426D, 1.44367D))
val pointsOfInterest = Seq(paris, toulouse)
val bayonne = PointOfInterst("Bayonne", Coordinates(43.48333D, -1.48333D))
damysos ++ pointsOfInterest + bayonne
```
The `++` method accepts a `TraversableOnce` argument, it means you can feed it either a normal
`Collection` (like the `Seq` above) or a lazy `Iterator`:
```scala
import damysos.PointOfInterst

val data: Iterator[PointOfInterst] = PointOfInterst.loadFromCSV("cities_world.csv")
damysos ++ data // Lines will be pulled one by one from the CSV file to be added to the Damysos
```
From there, we can start querying our data structure:
```scala
damysos contains bayonne

damysos findSurrounding paris
```
Note that `findSurrounding` also takes a `precision` argument to adjust the "zoom" level:
```scala
damysos.findSurrounding(paris, precision=4)
```
It also supports removing single element or a `TraversableOnce` of elements:
```scala
damysos - paris
damysos -- data
```

 ## Caveats

 (TODO: talk about "breakoff points" and resulting incomplete results)
