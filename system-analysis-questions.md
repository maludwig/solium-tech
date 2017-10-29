# Technical Interview: System Analysis Questions

## Part 1

### 1. What is the PID for the Java process using up the most RAM?

The PID is 21651, but let's just take a moment to appreciate this computer we're topping has 148423392k of RAM. That's like 141GB of RAM. This computer has more RAM than I have HDD space on the computer I'm typing on right now.

### 2. What is the PID for the Java process consuming the most CPU?

PID 22460

### 3. Is this system in distress?

I'd say "no", from this output...well, the processor is mostly idle, the swap is just sitting there, and basically never even had anything paged into it ever. But at the same time, a tech is looking at the output of top. I've never brought up top when a system is just, working properly...but I can tell you that if there is a problem with this system, it's not a CPU or RAM problem.

## Part 2

### In the below screenshot, what is the PID of the java process with the largest heap?

PID 24086. It has the largest lower bound and the largest higher bound on heap size.

## Part 3

### 1. In general, what are we looking at here?

This is looking at the memory usage statistics over time of a JVM, in the New Relic panel.

### 2. Explain the middle three graphs, Par Eden Space, Par Survivor, and CMS Old Gen heap usage.

Eden Space is the space where new Java objects are created, the Garden of Eden where seedlings grow. But some of them die as references are discarded. Garbage collection will check through Eden the most often. Those that survive Eden by their references persisting through a garbage collection, enter the Survivor Space where the GC looks at them more irregularly. Finally, when they get old in the Survivor Space, never being picked off by the GC, they move to the Old Gen heap space.

"Par" and "CMS" refer to the different types of garbage collection, Parallel and Concurrent Mark Sweep. 

### 3. Is the system in distress?

The system seems basically idle. The CPU seems like it's barely being used, and the RAM isn't really even changing. I wouldn't fret about this system.