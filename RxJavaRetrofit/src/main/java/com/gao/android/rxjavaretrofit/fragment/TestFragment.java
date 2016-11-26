package com.gao.android.rxjavaretrofit.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gao.android.rxjavaretrofit.R;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.distinctReduce)
    Button mDistinctReduce;
    @BindView(R.id.observableSubscriber)
    Button mObservableSubscriber;
    @BindView(R.id.rxViewClick)
    Button mRxViewClick;
    @BindView(R.id.rxTextView)
    EditText mRxTextView;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rxViewClick();
    }

    private void rxTextView() {
        // TODO: 2016/11/26 这个方法没有执行。textChanges和textChangeEvents区别
        RxTextView.textChanges(mRxTextView)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return charSequence.length() > 2;
                    }
                })
                .debounce(100, TimeUnit.MILLISECONDS)
                .switchMap(new Func1<CharSequence, Observable<List<String>>>() {
                    @Override
                    public Observable<List<String>> call(CharSequence charSequence) {
                        return makeApiCall(charSequence);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("onError");
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        Logger.d(strings);
                    }
                });

    }

    private Observable<List<String>> makeApiCall(CharSequence charSequence) {
        List<String> list  = new ArrayList();
        list.add("gao");
        list.add("cheng");
        list.add("quan");
        return  Observable.just(list);
    }

    @OnClick({R.id.map, R.id.flatMap, R.id.from, R.id.filterTake,
            R.id.debounce, R.id.timer, R.id.interval, R.id.merge,
            R.id.distinctReduce, R.id.observableSubscriber,
            R.id.rxTextView})
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
            case R.id.distinctReduce:
                distinctReduce();
                break;
            case R.id.observableSubscriber:
                observableSubscriber();
                break;
            case R.id.rxTextView:
                rxTextView();
                break;
        }
    }

    private void rxViewClick() {
        Subscription subscription = RxView.clicks(mRxViewClick)
                .throttleFirst(2, TimeUnit.SECONDS) //两秒钟之内只取一个点击事件，防止连续点击
                .subscribe(new Action1<Void>() {
                               @Override
                               public void call(Void aVoid) {
                                   Logger.d("rxViewClick");
                               }
                           }
                );

    }

    private void observableSubscriber() {
        Subscriber<String> subscriber = new Subscriber<String>() {
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
                Logger.d(s);
            }
        };
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("gao");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(subscriber);

        // 简写的方式
        Observable.just("hello", "shit")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Logger.d(s);
                    }
                });
    }

    private void distinctReduce() {
        Observable.just("1", "2", "2", "3", "4", "5")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return Integer.parseInt(s);
                    }
                })
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 1;
                    }
                })
                .distinct()
                .take(3)
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Logger.d(integer);
                    }
                });
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
