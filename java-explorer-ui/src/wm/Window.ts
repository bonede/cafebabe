import {
    DEFAULT_WINDOW_FRAME_STYLE,
    Point,
    Size,
    WindowClose,
    WindowEvent,
    WindowEventTag,
    WindowFrameStyle,
    WindowManager,
    WindowMaximize,
    WindowMinimize,
    WindowPaint,
    WindowResize
} from "./WindowManager";


export class WmWindow{
    get background(): string {
        return this._background;
    }
    get size(): Size {
        return this._size;
    }

    get pos(): Point {
        return this._pos;
    }
    private readonly _element: HTMLDivElement
    private readonly _key: number
    private _title: string
    private _size: Size
    private _pos: Point
    private _background: string
    get key(): number {
        return this._key;
    }

    get element(): HTMLDivElement {
        return this._element;
    }

    get title(): string {
        return this._title;
    }

    get style(): WindowFrameStyle {
        return this._style;
    }

    private readonly _style: WindowFrameStyle
    constructor(title: string, pos: Point, size: Size, background: string, style?: WindowFrameStyle) {
        this._key = Math.random()
        this._element = document.createElement("div")
        this._style = style == null ? DEFAULT_WINDOW_FRAME_STYLE : style
        this._title = title
        this._background = background
        this._pos = pos
        this._size = size
        WindowManager.windowManager.addWindow(this);
    }

    public getElement(): HTMLDivElement{
        return this._element;
    }

    public show(){

    }
    protected handleClose(event: WindowClose){

    }
    protected handleMinimize(event: WindowMinimize){

    }

    protected handleResize(event: WindowResize) {

    }

    protected handlePaint(event: WindowPaint) {
        this._element.innerText = "haha";
    }

    protected handleMaximize(event: WindowMaximize) {

    }

    private handleEvent(event: WindowEvent){
        switch (event.tag) {
            case WindowEventTag.WindowClose: return this.handleClose(event);
            case WindowEventTag.WindowMinimize: return this.handleMinimize(event)
            case WindowEventTag.WindowResize: return this.handleResize(event);
            case WindowEventTag.WindowPaint: return this.handlePaint(event);
            case WindowEventTag.WindowMaximize: return this.handleMaximize(event);
        }
    }

    public resize(size: Size){
        WindowManager.windowManager.requestResize(this._key, size)
    }


}

