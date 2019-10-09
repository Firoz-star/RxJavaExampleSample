package com.firozstar.rxjavaexamplesample;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = Main2Activity.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Observable<String> animalsObservable = getAnimalsObservable();

        DisposableObserver<String> animalsObserver = getAnimalsObserver();

        DisposableObserver<String> animalsObserverAllCaps = getAnimalsObserverAllCaps();

        compositeDisposable.add(
                animalsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("b");
                            }
                        })
                        .subscribeWith(animalsObserver));

        compositeDisposable.add(
                animalsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("c");
                            }
                        })
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(String s) throws Exception {
                                return s.toUpperCase();
                            }
                        })
                        .subscribeWith(animalsObserverAllCaps));

    }

    private DisposableObserver<String> getAnimalsObserver(){
     return new DisposableObserver<String>() {
         @Override
         public void onNext(String s) {
             Log.d(TAG, "Name: " + s);
         }

         @Override
         public void onError(Throwable e) {
             Log.d(TAG, "Error: " + e.getMessage());

         }

         @Override
         public void onComplete() {
             Log.d(TAG, "All items are emitted!");
         }
     };
    }


    private DisposableObserver<String> getAnimalsObserverAllCaps(){
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error: " + e.getMessage());

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");

            }
        };
    }

    private Observable<String> getAnimalsObservable(){
        return Observable.fromArray(
                "Ant", "Ape",
                "Bat", "Bee", "Bear", "Butterfly",
                "Cat", "Crab", "Cod",
                "Dog", "Dove",
                "Fox", "Frog");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }
}
