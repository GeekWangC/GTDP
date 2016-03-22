/**
 *@description 饼图
 *@author geekwangc
 *@data 2015年12月18日
 *@version 1.0.0
 */
var utils = {

    sort: function(raw, sorted) {
        var sortTable = [];
        for (key in raw) {
            sortTable.push([key, raw[key].figure]);
        }
        sortTable.sort(function(a, b) {return b[1] - a[1]});

        // resume other data field value
        for (key in sortTable) {
            var index = sortTable[key][0];
            sorted[index] = {};
            for (k in raw[index]) {
                sorted[index][k] = raw[index][k];
            }
        }
    },

    getTotal: function(raw) {
        var total = 0;
        for (key in raw) {
            total += raw[key].figure;
        }
        return total;
    },

    getPercentage: function(sorted, percentage, total) {
        for (key in sorted) {
            percentage[key] = sorted[key].figure / total;
        }
    },

    getRadius: function(deg) {
        return deg / 180 * Math.PI;
    },

};

/**
 * PieChart
 */
function PieChart(opt, fc) {
    this.ctx = fc.ctx;
    this.canvas = fc.canvas; 
    this.data = fc.data;
    this.cx = opt.cx || 100;                            // piechart x coordinate
    this.cy = opt.cy || 100;                            // piechart y coordinate
    this.r = opt.r || 100;                              
    this.lineWidth = opt.lineWidth || 50;  
    this.align = opt.align || 'center';
}

PieChart.prototype.draw = function() {
    this._align();
    this._animateDraw(this._drawPieChart);
};

PieChart.prototype._animateDraw = function(drawFunc) {
    var ctx = this.ctx;
    var startDeg = 180;
    var incre = 30;
    var self = this;

    var dr = setInterval(function() {
        ctx.save();
        
        ctx.clearRect(0,0,600,600);
        drawFunc.call(self, startDeg);
        startDeg += incre;

        if (startDeg >= 570) {
            clearInterval(dr);
            PieChart.prototype._drawLabel.call(self);
            PieChart.prototype._drawCenter.call(self); 
        }
        
        ctx.restore();
    }, 30);
};

// draw piechart
PieChart.prototype._drawPieChart = function(startDeg){
    var ctx = this.ctx;

    // var startDeg = -90;      // top degree is -90 degree
    var deg = 0;             // start degree
    var endDeg = 0;          // end degree
    var startRadius = 0;     // start radius
    var endRadius = 0;       // end radius
    var startPos = {'x': this.cx, 'y': this.r - this.y};    // start drawing position
    var endPos = {'x': 0, 'y': 0};                              // end line position
    this.currentDeg = 0;   //accumulated degrees for drawing icon

    for (key in this.data.percentage) {
        this.data.info[key] = {};
        deg = this.data.percentage[key] * 360;
        if (deg === 0) {
            continue;
        }
        endDeg = startDeg + deg;
        startRadius = utils.getRadius(startDeg);
        endRadius = utils.getRadius(endDeg);
        //store info
        this.data.info[key].deg = deg;
        this.data.info[key].startDeg = startDeg;
        this.data.info[key].endDeg = endDeg;
        this.data.info[key].startRadius = startRadius;
        this.data.info[key].endRadius = endRadius;

        // drawing pichart
        ctx.beginPath();
        ctx.moveTo(this.cx, this.cy);
        ctx.lineTo(startPos.x, startPos.y);
        ctx.arc(this.cx, this.cy, this.r, startRadius, endRadius, 0, 0);
        this._getPos(endDeg, endPos, this.r);
        ctx.fillStyle = this.data.sorted[key].color;
        ctx.fill();
        ctx.closePath();


        // next sector data
        startDeg = endDeg;
        startPos.x = endPos.x;
        startPos.y = endPos.y;

    }
};

// get end line of sector position
PieChart.prototype._getPos = function(currentDeg, lineToPos, r) {
    var radius = 0;
    var deg = 0;
    currentDeg += 90;

    if (currentDeg > 360) {
        currentDeg -= 360;
    }

    if (currentDeg <= 90) {
        deg = 90 - currentDeg;
        radius = utils.getRadius(deg);
        lineToPos.x = this.cx + Math.cos(radius) * r;
        lineToPos.y = this.cy - Math.sin(radius) * r;
    }
    else if (currentDeg <= 180) {
        deg = currentDeg - 90;
        radius = utils.getRadius(deg);
        lineToPos.x = this.cx + Math.cos(radius) * r;
        lineToPos.y = this.cy + Math.sin(radius) * r;
    }
    else if (currentDeg <= 270) {
        deg = 270 - currentDeg;
        radius = utils.getRadius(deg);
        lineToPos.x = this.cx - Math.cos(radius) * r;
        lineToPos.y = this.cy + Math.sin(radius) * r;
    }
    else if (currentDeg <= 360) {
        deg = currentDeg - 270;
        radius = utils.getRadius(deg);
        lineToPos.x = this.cx - Math.cos(radius) * r;
        lineToPos.y = this.cy - Math.sin(radius) * r;
    }
};

PieChart.prototype._align = function() {
    switch(this.align){
        case 'left':
            this.cx = this.r + this.lineWidth;
            break;
        case 'right':
            this.cx = this.canvas.clientWidth - this.r - this.lineWidth;
            break;
        default:
            this.cx = this.canvas.clientWidth / 2;
            break;
    }

    this.cy = this.canvas.clientHeight / 3;
};
// draw text
PieChart.prototype._drawCenter = function(){
    var ctx = this.ctx;
    var x = this.cx;
    var y = this.cy;
    ctx.font = "40px -apple-system-font, \"Helvetica Neue\", Helvetica, STHeiTi, sans-serif";
    ctx.fillStyle = "#000000";
    ctx.fillText(this.text.section, x -40*(this.text.section.length)/2, y - 80);
    ctx.fillText(this.text.sumdo, x - 18 , y );
    ctx.fillText(this.text.title, x -100, y +80);
}
// draw label and data
PieChart.prototype._drawLabel = function() {
    var ctx = this.ctx;
    switch(this.align){
        case 'left':
            var x = this.cx + this.r + 60;
            var y = this.cy - this.r;
            break;
        case 'right':
            var x = 60;
            var y = this.cy - this.r;
            break;
        default:
            var x = this.cx - this.r - 40;
            var y = this.cy + this.r + 130;
            break;
    }
    
    
    for (key in this.bottomData) {
        ctx.fillStyle = this.data.sorted[key].color;
        ctx.fillRect(x, y, 50, 50);
        PieChart.prototype._drawText.call(this, x, y, key);
        y += 100;
    }

    /*for (key in this.data.sorted) {
        ctx.fillStyle = this.data.sorted[key].color;
        ctx.fillRect(x, y, 50, 50);
        PieChart.prototype._drawText.call(this, x, y, key);
        y += 100;
    }*/
};


PieChart.prototype._drawText = function(x, y, key) {
    var ctx = this.ctx;
    ctx.font = "40px -apple-system-font, \"Helvetica Neue\", Helvetica, STHeiTi, sans-serif";
    ctx.fillStyle = "#000000";
    var showFigure = Math.round(this.data.percentage[key] * 10000) / 100;
    if(this.typeData == 1){
        var num = this.data.raw[key].figure;
        num = num == -1 ? 0 : num;
        ctx.fillText(key + " "+num, x + 50, y + 42);//
        ctx.fillText(showFigure + '%', x + 360, y + 42);
    }else{
        ctx.fillText(key, x + 50, y + 42);//
        ctx.fillText('占'+showFigure + '%', x + 360, y + 42);
    }
    
};

/**
 * RingChart
 */
function RingChart(opt, fc) {
    this.ctx = fc.ctx;
    this.canvas = fc.canvas; 
    this.data = fc.data;
    this.cx = opt.cx || 100;                            // piechart x coordinate
    this.cy = opt.cy || 100;                            // piechart y coordinate
    this.r = opt.r || 100;                              
    this.lineWidth = opt.lineWidth || 50;               // piechart radius
    this.align = opt.align || 'center';
    this.text = opt.text;
    this.typeData = opt.typeData;
    this.bottomData = opt.data;
}

RingChart.prototype.draw = function() {
    PieChart.prototype._align.call(this);
    PieChart.prototype._animateDraw.call(this, this._drawRingChart);
};

RingChart.prototype._drawRingChart = function(startDeg) {
    var ctx = this.ctx;
    // var startDeg = -90;
    var deg = 0;
    var endDeg = 0;
    var startRadius = 0;
    var endRadius = 0;
    var startPos = {'x': this.cx, 'y': this.r - this.y};    // start drawing position
    var endPos = {'x': 0, 'y': 0};                              // end line position
    this.currentDeg = 0;   //accumulated degrees for drawing icon

    for (key in this.data.percentage) {
        this.data.info[key] = {};
        deg = this.data.percentage[key] * 360;
        if (deg === 0) {
            continue;
        }
        endDeg = startDeg + deg;
        startRadius = utils.getRadius(startDeg);
        endRadius = utils.getRadius(endDeg);
        //store info
        this.data.info[key].deg = deg;
        this.data.info[key].startDeg = startDeg;
        this.data.info[key].endDeg = endDeg;
        this.data.info[key].startRadius = startRadius;
        this.data.info[key].endRadius = endRadius;

        // drawing pichart
        ctx.beginPath();
        ctx.strokeStyle = this.data.sorted[key].color;
        ctx.arc(this.cx, this.cy, this.r, startRadius, endRadius, 0);
        ctx.lineWidth = this.lineWidth;
        ctx.stroke();
        ctx.closePath();

        // next sector data
        startDeg = endDeg;
        startPos.x = endPos.x;
        startPos.y = endPos.y;

    }

};

function fdata(data) {
    this.raw = data;
    this.sorted = {};
    this.info = {};
    this.total = 0;
    this.percentage = {};
}

function fchart(opt) {
    this.canvas = opt.wrapper;                         // canvas
    this.ctx = opt.wrapper.getContext('2d');           // canvas context
    this.type = opt.type || 'piechart';
    this.text = opt.text;//中心文字
    this.data = new fdata(opt.data);
    this.data.total = utils.getTotal(this.data.raw);
    utils.sort(this.data.raw, this.data.sorted);
    utils.getPercentage(this.data.sorted, this.data.percentage, this.data.total);
    
    this.getWrapperSize();
    this.draw(opt);
}

// get wrapper size and set canvas size
fchart.prototype.getWrapperSize = function() {
    this.canvas.width = this.canvas.parentNode.clientWidth * 2 ;
    this.canvas.height = this.canvas.parentNode.clientHeight>434 ? 434 * 2 :this.canvas.parentNode.clientHeight*2;
    this.canvas.style.cssText = '-webkit-transform: translateX(-' + (this.canvas.width / 4) + 'px) scale(0.5);-webkit-transform-origin: 50% 0';
};

// draw canvas
fchart.prototype.draw = function(opt) {
    var obj;
    switch(this.type){
        case 'ringchart':
            obj = new RingChart(opt, this);
            break;
        case 'barchart':
            obj = new BarChart(opt, this);
            break;
        default:
            obj = new PieChart(opt, this);
            break;
    }

    obj.draw();
};
