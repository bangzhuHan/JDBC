package Bean;

/**
 * @author xh
 * @date 2022/4/21
 * @apiNote
 */
public class Student {
    private int flowID;
    private int type;
    private String IDCard;
    private String examCard;
    private String studentName;
    private String location;
    private int grade;

    public Student() {
    }

    public Student(int flowID, int type, String IDCard, String examCard, String studentName, String location, int grade) {
        this.flowID = flowID;
        this.type = type;
        this.IDCard = IDCard;
        this.examCard = examCard;
        this.studentName = studentName;
        this.location = location;
        this.grade = grade;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getFlowID() {
        return flowID;
    }

    @Override
    public String toString() {
        return  "========查询结果========\n"+ "流水号: " +
                + flowID +
                "\n四六级: " + type +
                "\n身份证号: " + IDCard +
                "\n准考证号: " + examCard +
                "\n学生姓名: " + studentName +
                "\n区域: " + location  +
                "\n成绩: " + grade
                ;
    }
}
