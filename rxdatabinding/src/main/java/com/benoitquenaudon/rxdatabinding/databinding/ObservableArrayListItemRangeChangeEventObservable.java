package com.benoitquenaudon.rxdatabinding.databinding;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.benoitquenaudon.rxdatabinding.internal.Preconditions.checkMainThread;

final class ObservableArrayListItemRangeChangeEventObservable<T>
    extends Observable<ObservableArrayListItemRangeChangeEvent<T>> {
  private final ObservableArrayList<T> observableArrayList;

  ObservableArrayListItemRangeChangeEventObservable(ObservableArrayList<T> observableArrayList) {
    this.observableArrayList = observableArrayList;
  }

  @Override
  protected void subscribeActual(
      Observer<? super ObservableArrayListItemRangeChangeEvent<T>> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(observableArrayList, observer);
    observer.onSubscribe(listener);
    observableArrayList.addOnListChangedCallback(listener.onListChangedCallback);
  }

  private final class Listener extends MainThreadDisposable {
    final ObservableList.OnListChangedCallback<ObservableArrayList<T>> onListChangedCallback;
    private final ObservableArrayList<T> observableArrayList;

    Listener(final ObservableArrayList<T> observableArrayList,
        final Observer<? super ObservableArrayListItemRangeChangeEvent<T>> observer) {
      this.observableArrayList = observableArrayList;
      this.onListChangedCallback =
          new ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {
            @Override public void onChanged(ObservableArrayList<T> observableArrayList) {
            }

            @Override
            public void onItemRangeChanged(ObservableArrayList<T> observableArrayList,
                int positionStart, int itemCount) {
              observer.onNext(
                  ObservableArrayListItemRangeChangeEvent.create(observableArrayList, positionStart,
                      itemCount));
            }

            @Override
            public void onItemRangeInserted(ObservableArrayList<T> observableArrayList,
                int positionStart, int itemCount) {
            }

            @Override
            public void onItemRangeMoved(ObservableArrayList<T> observableArrayList,
                int positionStart, int positionEnd, int itemCount) {
            }

            @Override
            public void onItemRangeRemoved(ObservableArrayList<T> observableArrayList,
                int positionStart, int itemCount) {
            }
          };
    }

    @Override protected void onDispose() {
      observableArrayList.removeOnListChangedCallback(onListChangedCallback);
    }
  }
}
