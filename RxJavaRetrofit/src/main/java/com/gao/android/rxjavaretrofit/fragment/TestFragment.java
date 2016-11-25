package com.gao.android.rxjavaretrofit.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gao.android.rxjavaretrofit.R;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by GaoMatrix on 2016/11/25.
 */
public class TestFragment extends Fragment {
    @BindView(R.id.map)
    Button mMap;
    @BindView(R.id.flatMap)
    Button mFlatMap;
    @BindView(R.id.from)
    Button mFrom;
    @BindView(R.id.filterTake)
    Button mFilterTake;
    @BindView(R.id.debounce)
    EditText mDebounce;
    @BindView(R.id.merge)
    Button mMerge;
    @BindView(R.id.timer)
    Button mTimer;
    @BindView(R.id.interval)
    Button mInterval;

    public static TestFragment newInstance() {

        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.map, R.id.flatMap, R.id.from, R.id.filterTake,
            R.id.debounce, R.id.timer, R.id.interval, R.id.merge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.map:
                map();
                break;
            case R.id.flatMap:
                flatMap();
                break;
            case R.id.from:
                from();
                break;
            case R.id.filterTake:
                filterTake();
                break;
            case R.id.debounce:
                debounce();
                break;
            case R.id.timer:
                timer();
                break;
            case R.id.interval:
                interval();
                break;
            case R.id.merge:
                merge();
                break;
        }
    }

    private void merge() {
        Observable.merge(getDataFromFile(), getDataFromMemory())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("onError");
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.d("onNext: " + s);
                    }
                });

    }

    private Observable<String> getDataFromFile() {
        return Observable.just("file");
    }

    private Observable<String> getDataFromMemory() {
        return Observable.just("memory");
    }

    private void interval() {
        Observable.interval(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("onError");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Logger.d("onNext");
                    }
                });
    }

    private void timer() {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("onError");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Logger.d("onNext");
                    }
                });
    }

    private void debounce() {
        RxTextView.textChangeEvents(mDebounce)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("onErrot");
                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        Logger.d("Searching for " + textViewTextChangeEvent.text().toString());
                    }
                });

    }

    private void filterTake() {
        query().flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return "addPre: " + s;
                    }
                })
                /*.flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return addPre(s);
            }
        })*/.filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.contains("a");
            }
        }).take(2)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.d("doOnNext: " + s);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.d(s);
                    }
                });

    }

    static Observable<List<String>> query() {
        List<String> list = Arrays.asList("Java", "Android", "C", "PHP", "ajjj", "map");
        return Observable.just(list);
    }

    private void flatMap() {
        query().flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                Logger.d(strings);//[Java, Android, C, PHP]
                return Observable.from(strings);
            }
        }).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return addPre(s);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.d(s);
            }
        });

    }

    private Observable<String> addPre(String s) {
        return Observable.just("addPre_" + s);
    }

    private void from() {
        List<String> list = Arrays.asList("Java", "Android", "C", "PHP");
        Observable.from(list)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.d(s);
                    }
                });
    }

    private void map() {
        Observable.just("Hello map operator")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        Logger.d(s);//Hello map operator
                        return 2016;
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        Logger.d(integer);//2016
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.d(s);//2016
                    }
                });

    }
}
