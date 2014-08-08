package com.example.sbeacons.app;

import android.app.Activity;
import android.webkit.JavascriptInterface;

/**
 * Created by a.dewan on 6/17/14.
 */
public class HTMLPAGE {

    Activity mContext;
    String htmlCode;

    String inlineJS ="var totalBeaconSize =0;\n" +
            "        var chart;\n" +
            "        var subChart;\n" +
            "        \n" +
            "        d3.json(\"http://7181ed81.ngrok.com/beacons/distinct\",function(error,json){\n" +
            "            if(error) return console.warn(error);\n" +
            "            \n" +
            "            for(var key in json){\n" +
            "                this.totalBeaconSize++;\n" +
            "            }\n" +
            "            getData();\n" +
            "        });\n" +
            "        \n" +
            "        \n" +
            "        function getData(){\n" +
            "          \n" +
            "            for(var i =1;i<=this.totalBeaconSize;i++){\n" +
            "                var timeData = []; \n" +
            "                var xData = [];\n" +
            "                var counter = 0;\n" +
            "                var name;\n" +
            "                if(i==1){\n" +
            "                    d3.json(\"http://7181ed81.ngrok.com/beacons?major=100&minor=\"+i,function(error,json){\n" +
            "                        if(error) return console.warn(error);\n" +
            "                        \n" +
            "                         var timeData = []; \n" +
            "                         var xData = [];\n" +
            "                         var counter = 0;\n" +
            "                         var name;\n" +
            "                        for(var key in json){\n" +
            "                            timeData[counter] = json[key].time;\n" +
            "                            xData[counter] = json[key].timeofday;\n" +
            "                            counter++;\n" +
            "                            name = json[key].name;\n" +
            "                        }\n" +
            "                        timeData.unshift(name);\n" +
            "                        xData.unshift(\"x\");\n" +
            "                        createChart(timeData,xData,0);\n" +
            "                    });\n" +
            "               }else{\n" +
            "                    d3.json(\"http://7181ed81.ngrok.com/beacons?major=100&minor=\"+i,function(error,json){\n" +
            "                        if(error) return console.warn(error);\n" +
            "                        \n" +
            "                        var timeData = []; \n" +
            "                        var xData = [];\n" +
            "                        var counter = 0;\n" +
            "                        var name;\n" +
            "                        for(var key in json){      \n" +
            "                            timeData[counter] = json[key].time;\n" +
            "                            xData[counter] = json[key].timeofday;\n" +
            "                            counter++;\n" +
            "                            name = json[key].name;\n" +
            "                        }\n" +
            "                        timeData.unshift(name); \n" +
            "                        createChart(timeData,xData,1)\n" +
            "                    });\n" +
            "               }\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        \n" +
            "        function createChart(timeData,xData,loadData){\n" +
            "            \n" +
            "            if(loadData ==0){\n" +
            "           this.chart = c3.generate({\n" +
            "                bindto: \"#chart\",\n" +
            "                data:{\n" +
            "                    x:'x',\n" +
            "                    columns:[\n" +
            "                        xData,\n" +
            "                        timeData\n" +
            "                    ],\n" +
            "                    type:'bar',\n" +
            "                    onclick: function(d){detailChart(d)}\n" +
            "                },\n" +
            "                subchart:{show:true},\n" +
            "                zoom:{\n" +
            "                    enabled:true\n" +
            "                },\n" +
            "                \n" +
            "                axis:{\n" +
            "                    x:{\n" +
            "                        type:'timeseries',\n" +
            "                        tick:{\n" +
            "                            format:'%m-%d-%Y'\n" +
            "                        }\n" +
            "                      }\n" +
            "                }\n" +
            "            });\n" +
            "         }else{\n" +
            "            this.chart.load({\n" +
            "                columns:[\n" +
            "                    timeData\n" +
            "                ]\n" +
            "            });\n" +
            "         }\n" +
            "        }\n" +
            "        \n" +
            "        function detailChart(d){\n" +
            "            var days = [\"Sunday\",\"Monday\",\"Tuesday\",\"Wednesday\",\"Thursday\",\"Friday\",\"Saturday\"];\n" +
            "            var date = new Date(d.x);\n" +
            "            var dateString = date.getFullYear()+\"-\"+(date.getMonth()+1)+\"-\"+date.getDate();\n" +
            "            var selectionIndex;\n" +
            "            var selectionName;            \n" +
            "            var j = 0;\n" +
            "            \n" +
            "            var dateDiv = document.getElementById('Date');\n" +
            "            dateDiv.innerHTML = days[date.getDay()]+\": \"+dateString;\n" +
            "            \n" +
            "            d3.json(\"http://7181ed81.ngrok.com/beacons/date?date=\"+dateString,function(error,json){\n" +
            "                    if(error) return console.warn(error);\n" +
            "                    for(var key in json){\n" +
            "                        var time =[];\n" +
            "                        time[0] = json[key].time;\n" +
            "                        time.unshift(json[key].name);\n" +
            "                        if(d.value == json[key].time){\n" +
            "                            selectionName = json[key].name;\n" +
            "                        }\n" +
            "                        if(j==0){\n" +
            "                            createDetailChart(time,0,selectionName);\n" +
            "                        }else{\n" +
            "                            createDetailChart(time,1,selectionName);\n" +
            "                        }\n" +
            "                        j++;\n" +
            "                    }\n" +
            "                 focus(selectionName);\n" +
            "            });\n" +
            "            \n" +
            "\n" +
            "        }\n" +
            "            \n" +
            "            function createDetailChart(timeData,loadData,selectionName){\n" +
            "            \n" +
            "            if(loadData ==0){\n" +
            "           this.subChart = c3.generate({\n" +
            "                bindto:\"#subchart\",\n" +
            "                data:{\n" +
            "                    columns:[\n" +
            "                        timeData\n" +
            "                    ],\n" +
            "                    selection:{\n" +
            "                        enabled: true\n" +
            "                    },\n" +
            "                    type:'pie',\n" +
            "                    subchart:{show:true}\n" +
            "                },\n" +
            "                \n" +
            "                zoom:{\n" +
            "                    enabled:true\n" +
            "                },\n" +
            "            });\n" +
            "         }else{\n" +
            "            this.subChart.load({\n" +
            "                columns:[\n" +
            "                    timeData\n" +
            "                ]\n" +
            "            });\n" +
            "         }\n" +
            "        }\n" +
            "            \n" +
            "        function focus(selectionName){\n" +
            "         setTimeout(function(){\n" +
            "            this.subchart.focus(selectionName);\n" +
            "            },1000);   \n" +
            "        }\n" +
            "        \n" +
            "        function toScatter(){\n" +
            "            if(document.getElementById(\"toScatter\").checked){\n" +
            "                this.chart.transform('scatter');\n" +
            "            }else{\n" +
            "                this.chart.transform('bar');\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        function toDonut(){\n" +
            "            if(document.getElementById(\"toDonut\").checked){\n" +
            "                this.chart.transform('donut');\n" +
            "            }else{\n" +
            "                this.chart.transform('bar');\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "         function toLine(){\n" +
            "            if(document.getElementById(\"toLine\").checked){\n" +
            "                this.chart.transform('line');\n" +
            "            }else{\n" +
            "                this.chart.transform('bar');\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        function toArea(){\n" +
            "            if(document.getElementById(\"toArea\").checked){\n" +
            "                this.chart.transform('area');\n" +
            "            }else{\n" +
            "                this.chart.transform('bar');\n" +
            "            }\n" +
            "        }";

    String HTML = "<html>\n" +
            "    <head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <link href=\"http://www.abhishekdewan.com/c3.css\" rel = \"stylesheet\" type =\"text/css\">\n" +
            "    <script src = \"http://www.abhishekdewan.com/c3.min.js\"></script>\n" +
            "    <script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"></script>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "    <input type=\"checkbox\" onchange = \"toScatter(this)\" id=\"toScatter\">Change to Scatter\n" +
            "    <input type=\"checkbox\" onchange = \"toDonut(this)\" id=\"toDonut\">Change to Donut\n" +
            "    <input type=\"checkbox\" onchange = \"toLine(this)\" id=\"toLine\">Change to Line\n" +
            "    <input type=\"checkbox\" onchange = \"toArea(this)\" id=\"toArea\">Change to Area\n" +
            "    <div id =\"chart\"></div>\n" +
            "    <div style=\"font-weight:bold;color:#000000;letter-spacing:1pt;word-spacing:-3pt;font-size:30px;text-align:left;font-family:helvetica, sans-serif;line-height:1;\" id = \"Date\"></div>\n" +
            "    <div id =\"subchart\"></div>\n" +
            "        <script>\n" +
                    inlineJS+
            "    </script>\n" +
            "    </body>\n" +
            "</html>\n";


    public HTMLPAGE(Activity c){
        mContext = c;
        htmlCode = HTML;
    }

    public String getHTML(){
        return HTML;
    }

    @JavascriptInterface
    public void showToast(String toast){
        mContext.runOnUiThread(new Runnable() {
            @Override
             public void run() {

            }
        });
    }
}
