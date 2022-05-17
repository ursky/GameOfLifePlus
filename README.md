# GameOfLife+

## What is this?
This repo contains a small Java implementation of an ecology multi-species interaction game/simulator. 
The purpose of the game is nothing more that provide a framework to model how relatively simple interactions between
a few types of organisms can produce complex patterns and dynamics. This simulator is loosely inspired by the classical
"Game of Life" - a mathematical 2D model that produces complex behavior from a set of extremely simple rules. My 
version of this concept lacks this simplicity, but instead features much more realistic creature movement, 
reproduction, movement, growth, interaction, and supports multi-species interactions such as 
predation, resource competition, sun shading, biomass distribution modeling, and others. 

## What is this for?
This is just a fun passion project I came up with as I was learning Java. In its current form the project 
has no real scientific value. However, since the game I built supports fairly complex interactions 
and extremely efficient for large-scale simulations, this game can theoretically be used to build a much more
realistic microbial interaction simulation.

## Is this a realistic ecology simulator?
This game has turtles that eat ladybugs, elephants that are the size of butterflies, and plants that don't need
water... No, its not realistic. I just used graphics and creature statistics that I thought were fun. 
The underlying core inter-species competition and predation mechanics are quite real, however. 

## What makes this impressive?
The entire simulation and visualization is build from the ground up in Java. The coding implementations used
throughout the project are low-level and carefully thought out with speed in mind. The final processing and rendering
time complexity is O(n), and memory is also O(n), which was quite difficult to achieve considering the complexity
of the interactions. Here are some of the simulation's features:
1. Every creature (bug, tree, grass, etc) is processed independently every frame (running at 60 frames per second).
This means 60 times every second, a given creature does all the game.things it can do at that moment, including:
move, accelerate towards something, run away from a predator (animals only), investigate the surrounding around, 
hunts prey (animals only), eats, grows, reproduces, lays eggs or spreads seeds, and shades neighbors (plants only). 
2. A novel custom implementation of a multi-threaded Quad Tree 2D search allows all the creatures to "see" other 
creatures around them in real time with a total time complexity of T=O(log n), which allows me to simultaneously 
calculate complex interactions between hundreds of thousands (!) of organisms.
3. Full multi-threading support - all steps of the simulation are done in parallel to improve performance. With 12 
cores I am able to simultaneously render and display ~100,000 organisms while maintaining a 50-60 frames per second
refresh rate.
4. Low-memory optimizations. The project uses pointers and some clever efficient memory caching throughout to be 
able to store millions (!!) of loaded organisms, each with their current parameters, movement, animations, etc.
5. Because the game allows for fast scrolling and zooming but can only realistically process ~250,000 creatures at 
a time, it saves creatures that are very far away from the player's current position into indexed bins.
Creatures stored this way are not updated while the player "is away", but will spring back to life if he/she returns 
to that location.
6. The field gets randomly populated to create the initial populations, however as the observer scrolls or zooms 
to previously un-initialized locations, the program will procedurally generate new populations to roughly match
those that were previously on-screen. 
7. All rates (i.e grows, speed, acceleration, etc.) in the game are handled with the current frame rate in
mind, which means the simulation will still work the same with tens of millions of creatures (it will just look more
choppy).
8. Each creature supports animations, as long as you have PNGs of each frame. As the creatures grow and move around
the images are automatically rotated, resized, change opacity, etc. (all done in O(1) time thanks to caching).
However, since I am not a digital artist the only creatures that are currently animated are the butterflies. 

## Example animations:
![Alt Text](animations/low_res_example_1.gif)

## Creature descriptions:
1. Grass: the smallest plant in the system. Very fast-growing, but is easily shaded out by larger plants. Eaten by 
elephants. Generally, grass will go extinct if it becomes out-competed for space.
2. Bush: the medium plant in the system. Fast-growing and prolific. Shades out grass and smaller tree saplings.
Eaten by caterpillars.
3. Tree: the largest plant. Slow-growing but has a large canopy that shades out all other plants except the strongest
of adult bushes. Will slowly cover the whole map unless kept in check by beetles.
4. Beetle: a quick bug that rapidly eats trees while young. Once an adult it stops eating and searches for new trees to
lay eggs under, which it does until it starves to death. Is predated on by turtles.
5. Caterpillar: the larval form of the butterfly. Very slow-moving and has poor senses. Eats bushes until it is fully
grown, at which point it turns into a pupae. Both the caterpillar and the pupae are predated on by the turtle.
6. Butterfly: the adult for of the caterpillar. Moves very fast thanks to flight. It does not eat, and will fly around
laying eggs on new bushes until it starves to death.
7. Elephant: a large last herbivore that eats grass. It is slow to reproduce but can survive food shortages.   
8. Turtle: the predator of the system, hunting young and adult beetles, caterpillars, and butterfly pupae. Is very slow
and vulnerable when young.

## How do I "play" and control the simulation?
Once you install the Java application and run `LaunchGame` the game will launch, randomly populate the world with some
creatures, and fast-forward a little to a point where the populations become more stable. From there you can 
observe the simulation and interface with it using the following controls:
1. Use "W", "A", "S", and "D" keys to scroll up, left, down, and to the right (respectively). 
2. Use "-" and "+" keys to zoom out or zoom in on the field.
3. Press the space bar to speed up the simulation. This part is really fun.
4. Use the mouse to select the creature icons in the bottom right and then left-click on the field to place new
creatures. It can be fun to mess with established communities this way. 

## Installation and running:
This is simply a Java project, so if you are comfortable, feel free to build and test however you prefer.
The only "special" dependency is that the game needs to be run inside `GameOfLifePlus/` so it can have access
to `GameOfLifePlus/graphics` and `GameOfLifePlus/rendered_images`. You will obviously need JRE installed on your
system. You can also load the project up into an IDE and launch `src/LaunchGame` directly.

However, if you would like a quick how-to guide, follow these steps (for Mac/Unix):
```bash
git clone https://github.com/ursky/GameOfLifePlus.git
cd GameOfLifePlus
./installGame.sh
./runGame.sh  # alternatively: `java -jar runGame.jar` 
```

## Author and credits
This game was independently developed by Gherman Uritskiy.
