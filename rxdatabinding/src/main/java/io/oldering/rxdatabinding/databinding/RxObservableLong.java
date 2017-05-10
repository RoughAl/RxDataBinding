package io.oldering.rxdatabinding.databinding;

import android.databinding.ObservableLong;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import io.reactivex.Observable;

import static io.oldering.rxdatabinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link
 * ObservableLong}.
 */
public final class RxObservableLong {
  private RxObservableLong() {
    throw new AssertionError("No instances.");
  }

  /**
   * Create an observable of property change events on {@code observableLong}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code observableLong}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull //
  public static Observable<Long> propertyChange(@NonNull ObservableLong observableLong) {
    checkNotNull(observableLong, "observableLong == null");
    return new ObservableLongObservable(observableLong);
  }
}