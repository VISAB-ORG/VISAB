# VISAB

<b>VISAB</b> stands for <b style="color: blue">VIS</b>ualizing <b style="color: green">A</b>gent <b>B</b>ehaviour</p>

VISAB is a standalone util to visualize artificial intelligence agent actions in video games. 

It aims to providing generic utilization and easy modifiability to integrate further games with minimal effort in the respective games.
VISAB can be used in two modes: 

<ol>
  <li><b>GUI Mode</b>: This mode starts VISABs HTTP-API such that data can be received and opens a window using which game data files can be visualized.</li>
  <li><b>Headless Mode</b>: This mode starts only VISABs HTTP-API.</li>
</ol>
 
### Extending VISAB for a new game
1. Create SessionListener by inheriting either `SessionListenerBase` or `ReplaySessionListenerBase` (if you also want a ReplayView)
2. Create the Statistics POJO (and a Image POJO if you need a Replay View)
3. Create a VISAB File by inheriting `BasicVISABFile`
4. Create a MainView containing a Tabcontrol with the view types you want to implement as tabs.
5. For each of your specific view types create a View + ViewModel pair with your ViewModel extending `VisualizeViewModelBase`.
Call `VisualizeViewModelBase` `initialize` method to read in the concrete file you created in 3.
```java
/**
 * Called by mvvmFx / javafx after creating the ViewModel instance, but before calling initialize in the view.
 */ 
public void initialize() {
    super.initialize(scope.getFile());
    ...
}
```
5. Lastly add your created classes to the classMapping.json file.

Thats it. None of the existing code has to be touched, were just adding new stuff.\
Since integration for new games is easy, the most time will likely be spend in implementing fitting visualizer views for your game. For examples check out the existing implmentation for Settlers of Catan or the CBRShooter.
