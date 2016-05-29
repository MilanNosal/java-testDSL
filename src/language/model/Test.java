package language.model;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Models the whole test.
 * @author Milan
 */
public class Test {
    
    public static final String NAME = "test";
    
    private String title;
    
    private int percMinimum;
    
    private List<AbstractMap.SimpleEntry<String, Question>> questions = new LinkedList<>();

    public Test(String title, int percMinimum) {
        this.title = title;
        if (percMinimum > 100 || percMinimum < 0) {
            throw new RuntimeException(NAME + " " + title + " percMinimum parameter requires to be between 0-100 (it is a percentage), you provided " + percMinimum + "!");
        }
        this.percMinimum = percMinimum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPercMinimum() {
        return percMinimum;
    }

    public void setPercMinimum(int percMinimum) {
        this.percMinimum = percMinimum;
    }

    public List<AbstractMap.SimpleEntry<String, Question>> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AbstractMap.SimpleEntry<String, Question>> questions) {
        this.questions = questions;
    }
    
    public void addQuestion(Question question) {
        AbstractMap.SimpleEntry<String, Question> entry 
                = new AbstractMap.SimpleEntry<>("question" + (questions.size() + 1), question);
        this.questions.add(entry);
    }
    
    public int getTotalPoints() {
        return questions.stream().map((q) -> q.getValue().getPoints()).reduce(0, (a, b) -> a + b);
    }
    
    public int getPassingMinimum() {
        return new Double(Math.ceil(getTotalPoints() * (this.percMinimum / 100.0))).intValue();
    }

    @Override
    public String toString() {
        return "Test{" + "title=" + title + ", percMinimum=" + percMinimum + ", questions=" + questions + '}';
    }
    
    public String toHTML() {
        String totalling = questions.stream().map((q) -> {            
            return "                    total += " + q.getValue().toJS(q.getKey()) + ";\n";
        }).reduce("", (a, b) -> a + b);
        
        String questionsHTML = questions.stream().map((q) -> q.getValue().toHTML(q.getKey())).reduce("", (a, b) -> a + "\n" + b);
        
        String retVal = "<!DOCTYPE html>\n"
+ "<html lang = 'en'>\n\n"
+ "<head>\n"
+ "    <title>" + title + "</title>\n\n"
+ "    <meta charset = 'utf-8'>\n"
+ "    <meta http-equiv = 'X-UA-Compatible' content = 'IE = edge'>\n"
+ "    <meta name = 'viewport' content = 'width = device-width, initial-scale = 1'>\n\n\n"
+ "    <!-- Bootstrap Core CSS -->\n"
+ "    <link rel='stylesheet' href='css/bootstrap.min.css' type='text/css'>\n\n"
+ "    <!-- Custom Fonts -->\n"
+ "    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>\n"
+ "    <link href='http://fonts.googleapis.com/css?family=Merriweather:400,300,300italic,400italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>\n"
+ "    <link rel='stylesheet' href='font-awesome/css/font-awesome.min.css' type='text/css'>\n\n"
+ "    <!-- Plugin CSS -->\n"
+ "    <link rel='stylesheet' href='css/animate.min.css' type='text/css'>\n\n"
+ "    <!-- Custom CSS -->\n"
+ "    <link rel='stylesheet' href='css/creative.css' type='text/css'>\n\n"
+ "    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\n"
+ "    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\n\n"
+ "    <!--[if lt IE 9]>\n"
+ "    <![endif]-->\n"
+ "    <!-- jQuery -->\n"
+ "    <script src='js/jquery.js'></script>\n\n"
+ "    <!-- Bootstrap Core JavaScript -->\n"
+ "    <script src='js/bootstrap.min.js'></script>\n\n"
+ "    <!-- Plugin JavaScript -->\n"
+ "    <script src='js/jquery.easing.min.js'></script>\n"
+ "    <script src='js/jquery.fittext.js'></script>\n"
+ "    <script src='js/wow.min.js'></script>\n\n"
+ "    <script src='js/bootbox.min.js'></script>\n"
+ "    <!-- Custom Theme JavaScript -->\n"
+ "    <script src='js/creative.js'></script>\n\n"
+ "    <script>\n"
+ "        function test() {\n"
+ "            bootbox.confirm('Are you sure you want to end your test?', function(result) {\n"
+ "                if (result) {\n"
+ "                    window.scrollTo(0, 0);\n"
+ "                    $('#submitBtn').hide();\n"
+ "                    full = " + getTotalPoints()+ ";\n"
+ "                    limit = " + getPassingMinimum() + ";\n"
+ "                    total = 0;\n"
+ totalling
+ "\n"
+ "                    $('input').each(function (index, value) {\n"
+ "                        $(value).attr('disabled', true);\n"
+ "                    });\n"
+ "                    $('select').each(function (index, value) {\n"
+ "                        $(value).attr('disabled', true);\n"
+ "                    });\n\n"
+ "                    $('#information-info').hide();\n"
+ "                    if (total >= limit) {\n"
+ "                        $('#success-points').text(total + '/' + full);\n"
+ "                        $('#success-info').fadeIn();\n"
+ "                    } else {\n"
+ "                        $('#failure-points').text(total + '/' + full);\n"
+ "                        $('#failure-info').fadeIn();\n"
+ "                    }\n"
+ "                }\n"
+ "            });\n"
+ "        }\n\n"
+ "        function testMultipleChoice(id) {\n"
+ "            selected = $(id);\n"
+ "            possiblePoints = parseFloat($(selected).attr('points'));\n\n"
+ "            allCorrect = selectedCorrect = 0;\n"
+ "            selectedIncorrect = false;\n\n"
+ "            $('input', selected).each(function (index, value) {\n"
+ "                toBeSelected = $.parseJSON($(value).attr('correct'));\n"
+ "                if (toBeSelected) {\n" +
"                    $(value).parent().parent().addClass('has-success');\n" +
"                } else {\n" +
"                    $(value).parent().parent().addClass('has-error');\n" +
"                }"
+ "                checked = $(value).is(':checked');\n"
+ "                if (toBeSelected) {\n"
+ "                    allCorrect += 1; // increment all\n"
+ "                    if (checked) {\n"
+ "                        selectedCorrect+= 1; // increment points\n"
+ "                    }\n"
+ "                } else {\n"
+ "                    if (checked) {\n"
+ "                        selectedIncorrect = true;\n"
+ "                    }\n"
+ "                }\n"
+ "            });\n\n"
+ "            points = 0;\n"
+ "            if (!selectedIncorrect) {\n"
+ "                points = (selectedCorrect * possiblePoints) / allCorrect;\n"
+ "                points = (Math.round(points * 100) / 100);\n"
+ "            }\n"
+ "            $('.points', selected).text(points + '/' + possiblePoints);\n"
+ "            return points;\n"
+ "        }\n\n"
+ "        function testSingleChoice(id) {\n"
+ "            selected = $(id);\n" +
"            possiblePoints = parseFloat($(selected).attr('points'));\n" +
"\n" +
"            selectedCorrect = false;\n" +
"\n" +
"            $('input', selected).each(function (index, value) {\n" +
"                correct = $.parseJSON($(value).attr('correct'));\n" +
"                if (correct) {\n" +
"                    $(value).parent().parent().addClass('has-success');\n" +
"                } else {\n" +
"                    $(value).parent().parent().addClass('has-error');\n" +
"                }\n" +
"                if ($(value).is(':checked')) {\n" +
"                    if (correct) {\n" +
"                        selectedCorrect = true;\n" +
"                    } else {\n" +
"                        selectedCorrect = false;\n" +
"                    }\n" +
"                }\n" +
"            });" +
"\n" +
"            points = selectedCorrect ? possiblePoints : 0;\n" +
"            points = (Math.round(points * 100) / 100);\n" +
"\n" +
"            $('.points', selected).text(points + '/' + possiblePoints);\n" +
"\n" +
"            return points;\n" +
"        }\n" +
"\n" +
"        function testFreeAnswer(id) {\n" +
"            selected = $(id);\n" +
"            possiblePoints = parseFloat($(selected).attr('points'));\n" +
"\n" +
"            correctAnswer = $('input', selected).attr('correctAnswer');\n" +
"            answer = $('input', selected).val();\n" +
"            caseSensitive = $.parseJSON($('input', selected).attr('caseSensitive'));\n" +
"\n" +
"            if (!caseSensitive) {\n" +
"                answer = answer.toLocaleUpperCase();\n" +
"                correctAnswer = correctAnswer.toLocaleUpperCase();\n" +
"            }\n" +
"\n" +
"            $('input', selected).parent().addClass((correctAnswer == answer) ? 'has-success' : 'has-error');\n" +
"\n" +
"            points = (correctAnswer == answer) ? possiblePoints : 0;\n" +
"            points = (Math.round(points * 100) / 100);\n" +
"            $('.points', selected).text(points + '/' + possiblePoints);\n" +
"\n" +
"            return points;\n" +
"        }\n" +
"\n" +
"        function testPairingAnswer(id) {\n" +
"            selected = $(id);\n" +
"            possiblePoints = parseFloat($(selected).attr('points'));\n" +
"\n" +
"            allQuestions = correctMatches= 0;\n" +
"\n" +
"            $('select', selected).each(function (index, value) {\n" +
"                allQuestions += 1;\n" +
"                selectedValue = $(value).val();\n" +
"                correctValue = $(value).attr('correct');\n" +
"                if (selectedValue == correctValue) {\n" +
"                    correctMatches += 1;\n" +
"                    $(value).parent().addClass('has-success');\n" +
"                } else {\n" +
"                    $(value).parent().addClass('has-error');\n" +
"                }\n" +
"            });\n" +
"\n" +
"            points = (correctMatches * possiblePoints) / allQuestions;\n" +
"            points = (Math.round(points * 100) / 100);\n" +
"            $('.points', selected).text(points + '/' + possiblePoints);\n" +
"\n" +
"            return points;\n" +
"        }\n" +
"    </script>\n" +
"\n" +
"    <style>\n" +
"        .slimFont {\n" +
"            min-height: 20px;\n" +
"            padding-left: 20px;\n" +
"            margin-bottom: 0;\n" +
"            font-weight: 400;\n" +
"            cursor: pointer;\n" +
"        }\n" +
"        .questionHeader {\n" +
"            padding: 10px;\n" +
"            margin-bottom: 5px;\n" +
"            border: 1px solid transparent;\n" +
"            border-radius: 4px;\n" +
"\n" +
"            background-color: #e0ebf0;\n" +
"            border-color: #c6cfe9;\n" +
"        }\n" +
"    </style>\n" +
"</head>\n" +
"\n" +
"<body>\n" +
"<div class = 'container'>\n" +
"<h1 class='text-center'>" + title + "</h1>\n" +
"\n" +
"    <div class='alert alert-success collapse' id='success-info'>\n" +
"        <strong>Congratulations!</strong> You have passed with <strong id='success-points'></strong> points.\n" +
"    </div>\n" +
"\n" +
"    <div class='alert alert-danger collapse' id='failure-info'>\n" +
"        <strong>Failure!</strong> You have failed with <strong id='failure-points'></strong> points (passing limit was at least " + getPassingMinimum() + " points).\n" +
"    </div>\n" +
"\n" +
"    <div class='alert alert-warning' id='information-info'>\n" +
"        <strong>Be careful!</strong> You will need <strong>" + getPassingMinimum() + "</strong> of total " + getTotalPoints() + " points to pass the test.\n" +
"    </div>\n" +
"\n" +
     questionsHTML +
"\n\n" +
"    <div style='margin:25px 0 25px 0;'>\n" +
"        <form class='form-horizontal' role='form' points='10'>\n" +
"            <div class='form-group'>\n" +
"                <div class='col-sm-offset-5 col-sm-1'>\n" +
"                    <button id='submitBtn' type='button' onclick='test()' class='btn btn-default'>Submit</button>\n" +
"                </div>\n" +
"            </div>\n" +
"        </form>\n" +
"    </div>\n" +
"</div>\n" +
"</body>\n" +
"</html>";
    
        return retVal;
    }
}
