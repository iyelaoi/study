package cn.wqz.thread.model;

public class Student {
    private String id;
    private String name;
    private int age;
    private int score;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Student(){}

    public Student(String id, String name, int age, int score) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        Student o = (Student)obj;
        if(this.id.equals(o.id) && this.name.equals(o.name) && this.age == o.age ){
            return true;
        }
        return false;
    }
}
