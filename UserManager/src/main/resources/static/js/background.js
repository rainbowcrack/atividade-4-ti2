const canvas = document.getElementById('bgCanvas');
const ctx = canvas.getContext('2d');
let width, height;
let points = [];

function resizeCanvas() {
    width = canvas.width = window.innerWidth;
    height = canvas.height = window.innerHeight;
    generatePoints();
}

function generatePoints() {
    points = [];
    const count = Math.floor((width * height) / 8000);
    for (let i = 0; i < count; i++) {
        points.push({
            x: Math.random() * width,
            y: Math.random() * height,
            vx: (Math.random() - 0.5) * 0.5,
            vy: (Math.random() - 0.5) * 0.5
        });
    }
}

function draw() {
    ctx.clearRect(0, 0, width, height);
    for (let i = 0; i < points.length; i++) {
        let p = points[i];
        p.x += p.vx;
        p.y += p.vy;

        if (p.x < 0 || p.x > width) p.vx *= -1;
        if (p.y < 0 || p.y > height) p.vy *= -1;

        ctx.beginPath();
        ctx.arc(p.x, p.y, 2, 0, Math.PI * 2);
        ctx.fillStyle = '#00faff';
        ctx.fill();

        for (let j = i + 1; j < points.length; j++) {
            let p2 = points[j];
            let dist = Math.hypot(p.x - p2.x, p.y - p2.y);
            if (dist < 100) {
                ctx.beginPath();
                ctx.moveTo(p.x, p.y);
                ctx.lineTo(p2.x, p2.y);
                ctx.strokeStyle = `rgba(0, 250, 255, ${1 - dist / 100})`;
                ctx.stroke();
            }
        }
    }

    requestAnimationFrame(draw);
}

window.addEventListener('resize', resizeCanvas);
resizeCanvas();
draw();
