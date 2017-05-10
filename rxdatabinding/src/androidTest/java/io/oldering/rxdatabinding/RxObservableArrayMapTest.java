package io.oldering.rxdatabinding;

import android.databinding.ObservableArrayMap;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import io.oldering.rxdatabinding.internal.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class) //
public class RxObservableArrayMapTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  @Test @UiThreadTest public void mapChangedEvents() {
    final ObservableArrayMap<String, String> observableArrayMap = new ObservableArrayMap<>();
    String key = "key";
    String value = "value";
    ObservableArrayMapOnMapChangedEvent<String, String> next;

    RecordingObserver<ObservableArrayMapOnMapChangedEvent<String, String>> o =
        new RecordingObserver<>();
    RxObservableArrayMap.mapChangedEvents(observableArrayMap).subscribe(o);
    o.assertNoMoreEvents();

    observableArrayMap.put(key, value);
    next = o.takeNext();
    assertEquals(observableArrayMap, next.observableArrayMap());
    assertEquals(key, next.key());

    value = "corrupted";
    observableArrayMap.put(key, value);
    next = o.takeNext();
    assertEquals(observableArrayMap, next.observableArrayMap());
    assertEquals(key, next.key());

    observableArrayMap.put(key + "2", value + "2");
    o.takeNext();
    observableArrayMap.remove(key);
    next = o.takeNext();
    assertEquals(observableArrayMap, next.observableArrayMap());
    assertEquals(key, next.key());

    observableArrayMap.clear();
    next = o.takeNext();
    assertEquals(observableArrayMap, next.observableArrayMap());
    assertNull(next.key());

    o.dispose();

    observableArrayMap.put(key, value);
    o.assertNoMoreEvents();
  }
}
