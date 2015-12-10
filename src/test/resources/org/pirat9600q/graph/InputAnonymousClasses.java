package org.pirat9600q.graph;

public class InputAnonymousClasses {

    public void method() {
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                a(); //calling anonymous class`s a() method, not InputAnonymousClasses.a();
                a("asdsd");
                InputAnonymousClasses.this.a();
                InputAnonymousClasses.this.a("asds");
                topLevelMethod();
            }

            void a() {

            }

            void a(String s) {

            }
        };
    }

    public void a() {

    }

    public void a(String s) {

    }

    public void topLevelMethod() {

    }
}
