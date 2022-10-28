import {WmWindow} from "./Window";
import './wm.css'

export interface Point{
    x: number
    y: number
}

export interface Size{
    width: number
    height: number
}
export enum WindowEventTag{
    WindowMinimize,
    WindowMaximize,
    WindowClose,
    WindowPaint,
    WindowResize
}
export interface WindowMaximize
{
    tag: WindowEventTag.WindowMaximize
    size: Size
}
export interface WindowClose{
    tag: WindowEventTag.WindowClose
}
export interface WindowMinimize{
    tag: WindowEventTag.WindowMinimize
}
export interface WindowPaint{
    tag: WindowEventTag.WindowPaint
    pos: Point
    size: Size
}
export interface WindowResize{
    tag: WindowEventTag.WindowResize
    pos: Point
    size: Size
}

export type WindowEvent = WindowClose | WindowMinimize | WindowResize | WindowPaint | WindowMaximize
enum WindowFrameStatus{
    Hidden,
    Normal,
    Inactive,
    Minimized,
    Maximized,
    Closed,
}
export interface WindowFrameStyle{
    frame: boolean
    closeable: boolean
    minimizable: boolean
    maximizable: boolean
    resizable: boolean
    titleBar: boolean
    menubar: boolean
}
export let DEFAULT_WINDOW_FRAME_STYLE: WindowFrameStyle = {
    frame: true,
    closeable: true,
    minimizable: true,
    maximizable: true,
    resizable: true,
    titleBar: true,
    menubar: true
}
class Rect{

    div: HTMLDivElement
    pos?: Point
    size: Size


    public className: string
    public children: Rect[] = []
    constructor(size: Size, className: string, pos?: Point) {
        this.div = document.createElement("div")
        this.div.className = className
        this.className = className;
        this.size = size
        this.setPos(pos)
        this.setSize(size)
        this.setPos(pos)
    }


    public setPos(pos?: Point){
        if(pos){
            this.pos = pos
            this.div.style.transform = `translate(${pos.x}px, ${pos.y}px)`
        }

    }

    public setSize(size: Size){
        this.div.style.width = size.width + "px"
        this.div.style.height = size.height + "px"
        this.size = size
    }

    public addChild(rect: Rect){
        this.children.push(rect)
        this.div.appendChild(rect.div)
    }
}
class WindowFrame{
    frame: Rect
    titleBar: Rect
    content: Rect
    mouseOrigin?: Point
    oldOrigin?: Point
    preMousePos?: Point
    mousePos: Point = {
        x: 0,
        y: 0
    }
    mouseMove: Point = {
        x: 0,
        y: 0
    }
    preTs?: DOMTimeStamp
    constructor(status: WindowFrameStatus, zIndex: number, window: WmWindow) {
        this.status = status;
        this.zIndex = zIndex;
        this.pos = window.pos;
        this.size = window.size;
        let titleBarHeight = 22;
        this.frame = new Rect(
        {
                width: window.size.width,
                height: window.size.height + titleBarHeight
            },
            "wm-frame",
            window.pos
        )
        this.titleBar = new Rect( {height: titleBarHeight, width: window.size.width}, "jswm-window-title-bar")
        this.titleBar.div.innerHTML = window.title
        this.content = new Rect(window.size, "wm-frame")
        this.content.div.style.background = window.background
        this.frame.addChild(this.titleBar)
        this.frame.addChild(this.content)
        document.body.addEventListener("mouseup", this.handleTitleBarMouseUp.bind(this))
        document.body.addEventListener("mousemove", this.handleTitleBarMouseMove.bind(this))
        document.body.addEventListener("mousedown", this.handleTitleBarMouseDown.bind(this))

    }
    public handleTitleBarMouseDown(){
        window.requestAnimationFrame(this.handleAnimationFrame.bind(this))
    }

    public handleTitleBarMouseUp(){
        this.mouseOrigin = undefined
        this.oldOrigin = undefined
    }

    public handleAnimationFrame(ts: DOMTimeStamp){
        // let dx = this.mousePos!.x - this.preMousePos!.x
        // let dy = this.mousePos!.y - this.preMousePos!.y
        // this.frame.setPos({
        //     x: this.mousePos!.x,
        //     y: this.mousePos!.y,
        // })
        this.frame.setPos({
            x: this.mousePos.x,
            y: this.mousePos.y
        })
        window.requestAnimationFrame(this.handleAnimationFrame.bind(this))
    }

    public handleTitleBarMouseMove(e: MouseEvent){
        this.mouseMove.x = e.movementX
        this.mouseMove.y = e.movementY
        this.mousePos.x = e.clientX + 10
        this.mousePos.y = e.clientY + 10
    }

    status: WindowFrameStatus
    zIndex: number
    pos: Point
    size: Size
}

export interface WindowManagerOpts{
    background: string
}
export class WindowManager {
    public static windowManager: WindowManager;
    private canvasDiv: HTMLDivElement;
    public constructor(opts?: WindowManagerOpts) {
        window.onresize = this.handleWindowResize.bind(this);
        this.canvasDiv = document.createElement("div")
        this.canvasDiv.className = "jswm-canvas";
        document.body.appendChild(this.canvasDiv);
        this.handleWindowResize();
        if(opts){
            this.canvasDiv.style.background = opts?.background;
        }

        let canvas = document.createElement("canvas")
        canvas.width = 400
        canvas.height = 400
        let ctx = canvas.getContext("2d")!
        ctx.fillRect(0, 0, 400, 400)

        let mouseX = 0
        let mouseY = 0
        let animation = () => {
            ctx.fillStyle = "#000"
            ctx.fillRect(0, 0, 400, 400)
            ctx.fillStyle = "#fff"
            ctx.fillRect(mouseX, mouseY, 10, 10)
            window.requestAnimationFrame(animation)
        }
        canvas.addEventListener("mousedown", animation)
        canvas.addEventListener("mousemove", (e) => {
            mouseX = e.clientX
            mouseY = e.clientY
        })
        this.canvasDiv.appendChild(canvas)
    }

    private handleWindowResize(){
        let size = this.getViewPortSize();
        this.setDivSize(this.canvasDiv, size)
    }

    private windowFrames : {
        [key: string]: WindowFrame;
    } = {};

    private createDiv(className: string): HTMLDivElement{
        let div = document.createElement("div");
        div.className = className;
        return div;
    }

    private setDivSize(div: HTMLDivElement, size: Size){
        div.style.width = size.width + "px"
        div.style.height = size.height + "px"
    }

    private setDivPos(div: HTMLDivElement, pos: Point){
        div.style.top = pos.y + "px"
        div.style.height = pos.x + "px"
    }

    public addWindow(window: WmWindow){
        let windowFrame = new WindowFrame(WindowFrameStatus.Normal, 99, window)
        this.canvasDiv.appendChild(windowFrame.frame.div)
        this.windowFrames[window.key] = windowFrame
    }
    public createFrame(){

    }

    public showWindow(key: string){

    }

    public requestResize(key: number, size: Size){

    }

    public requestPos(key: string, pos: Point){

    }

    public getViewPortSize(): Size{
        return {
            width: window.innerWidth,
            height: window.innerHeight
        }
    }


}