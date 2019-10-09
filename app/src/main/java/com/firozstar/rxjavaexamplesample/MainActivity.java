package com.firozstar.rxjavaexamplesample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    /**
     * Basic Observable, Observer, Subscriber example
     * Observable emits list of animal names
     * You can see Disposable introduced in this example
     */

    private static final String TAG = MainActivity.class.getSimpleName();

    private Disposable disposable;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //observable
        Observable<String> animalsObservable = getAnimalsObservable();

        // observer
        Observer<String> animalsObserver = getAnimalsObserver();

        // observer subscribing to observable
        animalsObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                //.subscribe(animalsObserver);
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return s.toLowerCase().startsWith("b");
                    }
                })

                .subscribeWith(animalsObserver);
    }

    private Observer<String> getAnimalsObserver(){
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG,"onSubscriber");
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");

            }
        };
    }

    private Observable<String> getAnimalsObservable() {
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

        // don't send events once the activity is destroyed
        Log.d(TAG,"don't send the events once the activity is destroyed");
        disposable.dispose();

    }
}
