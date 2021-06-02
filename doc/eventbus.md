# Eventbus Subscriber and Publisher Information

For EventBus, Event and Publisher structure mainly refer to the UML diagram. This documents main purpose is to show the subscribers.

### EventBus
* `eventbus.ApiEventBus`

### Events
* `eventbus.event.ImageReceivedEvent`
* `eventbus.event.SessionOpenedEvent`
* `eventbus.event.SessionClosedEvent`
* `eventbus.event.StatisticsReceivedEvent`

### Publishers
1. `api.SessionWatchdog`
    * `eventbus.event.SessionOpenedEvent`
    * `eventbus.event.SessionClosedEvent`
2. `api.controller.StatisticsController`
    * `eventbus.event.StatisticsReceivedEvent`
3. `api.controller.MapController`
    * `eventbus.event.ImageReceivedEvent`

### Subscribers
More subscribers can be added as seen fit. Here listed are the subscribers that are mandatory for a functioning VISAB. All the listed classes use the `eventbus.ApiEventBus` as their bus.
You may not only subscribe concrete event types, but also interfaces implementing IEvent.
In our case, the `WebApiViewModel` suscribes the `IApiEvent` and therefore is notified, whenever any ApiEvent occurs.


#### Mandatory

1. `processing.SessionListenerFactory`
    * `eventbus.event.SessionOpenedEvent`
2. `processing.SessionListenerBase`
    * `eventbus.event.SessionClosedEvent`
    * `eventbus.event.StatisticsReceivedEvent`
3. `processing.ReplaySessionListenerBase`
    * `eventbus.event.SessionClosedEvent`
    * `eventbus.event.StatisticsReceivedEvent`
    * `eventbus.event.ImageReceivedEvent`
4. `api.SessionWatchdog`
    * `eventbus.event.StatisticsReceivedEvent`

#### Optional

1. `newgui.webapi.WebApiViewModel`
    * `eventbus.IApiEvent`



