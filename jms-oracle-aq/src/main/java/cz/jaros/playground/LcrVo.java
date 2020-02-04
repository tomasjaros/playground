package cz.jaros.playground;

public class LcrVo {

    private String foo;
    private String bar;

    public LcrVo() {
        System.out.println("Creating LcrVo");
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
