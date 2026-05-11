# AAToolJava: CTM's AATool, but in Java!

[CTM's AATool](https://github.com/DarwinBaker/AATool) is a popular software among Minecraft All Advancements speedrunners. According to the repository's README, it's "a standalone program capable of tracking the advancements, statistics, and blocks of one or more players, either on a local world or on a server over the internet", with support for lots of Minecraft versions and speedrun categories.

However, one aspect where the support falls short is OS compatibility: the [relevant section](https://github.com/DarwinBaker/AATool#compatibility) of the README explicitly states that "this tool is only supported on Microsoft Windows", because it creates the GUI with Windows-only technologies, like Windows Forms and the [Microsoft XNA Framework](https://en.wikipedia.org/wiki/Microsoft_XNA), which in turn uses the DirectX APIs.

The goal of this project is to bring AATool to all platforms, so that speedrunning the All Advancements category is accessible to Linux and macOS players, too; I'm trying to do it by manually translating the code to Java, while rewriting the GUI part so that it uses the cross-platform tools provided by the language. The feasibility of a consistent GUI across operating systems is proven by the existence of another well-known Minecraft speedrunning tool, [Ninjabrain Bot](https://github.com/Ninjabrain1/Ninjabrain-Bot), which is in fact written in Java.

## The plan :pencil:

In order to tackle the project more easily, I'm mentally dividing it into three parts:

- the filesystem/network interface, which is responsible for finding a local or remote world folder, listening to changes to the advancements and statistics files and extracting the necessary data from them;
- the business logic, which simply processes the data in various ways to determine the speedrun progress;
- the GUI, which shows the progress with the fancy dashboard that AATool is known for.

I'm going to translate the logic first, because it should be the most straightforward part, then the data extraction, which involves converting calls to .NET APIs into the Java equivalents, and finally the user interface: a rewrite will probably be needed, but the aim is still to get as close as possible to the original.
