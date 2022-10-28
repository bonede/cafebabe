import './wm.css'

export class Window{
    id: number
    options: WindowOptions
    owner: JsWm
    contentElement: HTMLElement
    eventListeners: Map<WindowEvent, WindowEventListener[]> = new Map<WindowEvent, WindowEventListener[]>();
    constructor(options: WindowOptions, owner: JsWm) {
        this.options = options
        this.owner = owner
        this.id = 10;
        this.contentElement = document.createElement("div")
        this.contentElement.className = "jswm-window-content"
    }

    public addEventListener(event: WindowEvent , eventListener: WindowEventListener){
        if(!this.eventListeners.get(event)){
            this.eventListeners.set(event, [])
        }
        this.eventListeners.get(event)!.push(eventListener)
    }

    public removeEventListener(event: WindowEvent, eventListener: WindowEventListener){
        if(this.eventListeners.get(event)){
            this.eventListeners.set(event, this.eventListeners.get(event)!.filter(e => e == e))
        }
    }

    public close(){

    }

    public minimize(){

    }

    public dock(dockOption: DockOption){

    }

    public setContent(content: HTMLElement){

    }

    public broadcast(data: any){
        this.owner.broadcast(data, this);
    }

    public dispatchEvent(event: WindowEvent, data: WindowEventData){
        if(this.eventListeners.get(event)){
            for(let i = 0; i < this.eventListeners.get(event)!.length; i++){
                this.eventListeners.get(event)![i](event, data)
            }
        }
    }

    public render(){

    }


}

export type WindowEventListener = (event: WindowEvent, data: WindowEventData) => number;
export interface WindowOptions {
    showTitleBar: boolean
    titleBar: string
    showScrollBar: boolean
    showMenu: boolean
    canMove: boolean
    canResize: boolean
    canClose: boolean
    canMinimize: boolean
    canDock: boolean
    canBringToFront: boolean
    size: Size,
    position: Point
}
export enum DockPosition{
    Float,
    Top,
    Left,
    Right,
    Bottom,
    Tab
}
export enum WindowsState{
    Closed,
    Active,
    Minimized
}
export interface Size{
    width: number
    height: number
}
export interface WindowProp{
    id: number
    x: number
    y: number
    height: number
    width: number
    option: WindowOptions
    state: WindowsState
    content: HTMLElement
}
type WindowResize = [x: number, y: number];

export enum WindowEvent{
    Resize,
    Close,
    Minimize,
    Dock,
    BroadCast
}

export interface WindowEventData{
    x?: number
    y?: number
    window?: Window
    data?: any
}
export enum KeyCode{

}
export interface MenuItem{
    disabled: boolean
    checked: boolean
    isDivider: boolean
    shortCut?: KeyCode[]
    children?: MenuItem[]
    icon?: HTMLElement
}

export interface MenuBar{
    menuItems: MenuItem[]
}
export interface JsWmOptions {

}
export interface DockTarget{
    window: Window
    dockPosition: DockPosition
}
export type DockOption = DockTarget | DockPosition
export interface Point{
    x: number
    y: number
}
export class WindowNode {
    window: Window
    childWindows: Map<DockPosition, WindowNode[]> = new Map<DockPosition, WindowNode[]>();
    titleBarHeight = 20
    size: Size = {
        width: 0,
        height: 0
    }
    position: Point = {
        x: 0,
        y: 0
    }
    anchorElement: HTMLElement
    titleBarElement: HTMLElement
    public getTitleBarHeight(): number{
        return this.window.options.showTitleBar ? this.titleBarHeight : 0;
    }
    constructor(window: Window) {
        this.window = window;
        this.childWindows.set(DockPosition.Tab, [])
        this.childWindows.set(DockPosition.Top, [])
        this.childWindows.set(DockPosition.Bottom, [])
        this.childWindows.set(DockPosition.Left, [])
        this.childWindows.set(DockPosition.Right, [])
        this.anchorElement = document.createElement("div")
        this.anchorElement.className = "jswm-window"
        this.titleBarElement = document.createElement("div")
        this.titleBarElement.className = "jswm-window-title-bar"
        this.titleBarElement.append(window.options.titleBar)
        this.setSize({
            width: window.options.size.width,
            height: this.getTitleBarHeight() + window.options.size.height
        })
        this.toggleTitleBar(window.options.showTitleBar)
        this.anchorElement.appendChild(this.titleBarElement)
        this.anchorElement.appendChild(window.contentElement)

    }

    public toggleTitleBar(showTitleBar: boolean){
        if(showTitleBar){
            this.titleBarElement.style.display = "block"
        }else{
            this.titleBarElement.style.display = "none"
        }
    }




    public static of(window: Window){
        return new WindowNode(window)
    }

    public setSize(size: Size){
        this.size = size
        this.anchorElement.style.width = size.width + "px"
        this.anchorElement.style.height = size.height + "px"
    }

    public setPosition(point: Point){
        this.position = point
        this.anchorElement.style.top = point.y + "px"
        this.anchorElement.style.left = point.x + "px"
    }

    public setBackground(cssBackground: string){
        this.anchorElement.style.background = cssBackground;
    }
}
export class JsWm{
    rootWindowNode: WindowNode
    windowNodeMap: Map<Number, WindowNode>
    options: JsWmOptions
    public getViewPortSize(): Size{
        return {
            width: window.innerWidth,
            height: window.innerHeight
        }
    }
    public constructor(options: JsWmOptions) {
        this.options = options

        let rootWindow = new Window({
            showMenu: false,
            showTitleBar: true,
            titleBar: "haha",
            showScrollBar: false,
            canMove: false,
            canMinimize: false,
            canBringToFront: false,
            canDock: false,
            canResize: false,
            canClose: false,
            size: this.getViewPortSize(),
            position: {
                x:0,
                y: 0
            }
        }, this)
        this.rootWindowNode = WindowNode.of(rootWindow)
        this.windowNodeMap = new Map<Number, WindowNode>();
        this.windowNodeMap.set(rootWindow.id, this.rootWindowNode)
        window.onresize = this.handleWindowResize.bind(this);
        this.rootWindowNode.setBackground("#cccccc")
        this.renderRootWindow()
    }

    public handleWindowResize(){
        this.rootWindowNode.setSize(this.getViewPortSize())
    }

    public addWindow(options: WindowOptions, content: HTMLElement, dockTarget: DockTarget): Window{
        let window = new Window(options, this)
        let windowNode = WindowNode.of(window)
        let dockWindowTarget = dockTarget.window ? dockTarget.window : this.rootWindowNode.window
        if(dockTarget.window){
            let targetWindowNode = this.windowNodeMap.get(dockTarget.window.id)
            if(!targetWindowNode){
                throw new Error("Target window is not mounted. Window id: " + window.id)
            }
            targetWindowNode.childWindows.get(dockTarget.dockPosition)!.push(windowNode)
        }else{
            this.rootWindowNode.childWindows.get(dockTarget.dockPosition)!.push(windowNode)
        }
        this.windowNodeMap.set(window.id, windowNode)
        return window
    }

    public createWindowDiv(className: string){
        let div = document.createElement("div")
        div.className = "jswm-window " + className
        return div
    }

    public createCanvasDiv(){
        let div = document.createElement("div")
        div.className = "jswm-canvas"
        return div
    }
    public renderRootWindow(){
        let canvas = this.createCanvasDiv()
        canvas.appendChild(this.rootWindowNode.anchorElement)
        document.body.appendChild(canvas)
    }

    public renderWindow(window: Window){
        window.render()
    }

    public removeWindow(window: Window){

    }

    public broadcast(data: any, from?: Window){
        let eventData: WindowEventData = {
            data: data,
            window: from
        }
        this.windowNodeMap
            .forEach(w => w.window.dispatchEvent(WindowEvent.BroadCast, eventData))

    }



}