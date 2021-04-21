const editing_window = document.getElementById("editing_window");
const txtBox = document.getElementById("text-box-field");

const left = parseInt(editing_window.style.left);
const top = parseInt(editing_window.style.top);

let offsetX;
let offsetY;


function txtBoxDrag(e) {
    e = e || window.event;
    const rect = e.target.getBoundingClientRect();
    offsetX = e.clientX - rect.x;
    offsetY = e.clientY - rect.y;
    
}

/*function txtBoxDrop(e) {
    e = e || window.event;
    e.preventDefault();
    txtBox.style.position = 'absolute';
    txtBox.style.left = 
}*/