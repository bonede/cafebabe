import './panel.css'
import {addClass, createDiv, removeClass, setHeight, setWidth} from "../Utils";


export class Panel{
    contents: PanelContent[]
    tabIndex: number = 0
    size: number
    tabItemDivs: HTMLDivElement[] = []
    bodyContent: HTMLElement[] = []
    rootDiv = createDiv("pl-panel")
    titleDiv = createDiv("pl-panel-title")
    bodyDiv = createDiv("pl-panel-body")
    footerDiv = createDiv("pl-panel-footer")
    rightDiv = createDiv("pl-panel-right")
    rightIcons = createDiv("pl-panel-right-icons")
    showTitle = true
    showFooter = true

    constructor(content: PanelContent[], size: number, showTitle: boolean = true, showFooter: boolean = true) {
        this.contents = content
        this.size = size
        this.showTitle = showTitle
        this.showFooter = showFooter
        if(this.contents.length < 1){
            throw new Error("Contents can not be empty")
        }
    }

    onTabChange(tabIndex: number){
        if(this.showTitle){
            removeClass(this.tabItemDivs[this.tabIndex], "current")
            if(this.bodyDiv.firstChild){
                this.bodyDiv.removeChild(this.bodyDiv.firstChild)
            }
            this.tabIndex = tabIndex
            addClass(this.tabItemDivs[tabIndex], "current")
        }

        if(!this.bodyContent[tabIndex]){
            let content = this.contents[this.tabIndex]
            if(content.body instanceof HTMLElement){
                this.bodyContent[tabIndex] = content.body
            }else if(content.body instanceof PanelGroup) {
                this.bodyContent[tabIndex] = content.body.render()
            }
        }
        this.bodyDiv.appendChild(this.bodyContent[tabIndex])
    }
    onTabClick(e: MouseEvent){
        let div = e.target
        for(let i = 0; i < this.tabItemDivs.length; i++){
            let tabItem = this.tabItemDivs[i];
            if(tabItem == div){
                this.onTabChange(i)
            }
        }
    }
    tabView(): HTMLDivElement{
        let tab = createDiv("pl-tab")
        if(this.contents.length > 1){
            addClass(tab, "tab")
        }
        for(var i = 0; i < this.contents.length; i++){
            let content = this.contents[i]
            let tabItem = createDiv("pl-tab-item")
            tabItem.innerText = content.title
            if(this.tabIndex == i){
                addClass(tabItem, "current")
            }
            tab.appendChild(tabItem)
            tab.addEventListener("click", this.onTabClick.bind(this))
            this.tabItemDivs.push(tabItem)
        }
        return tab
    }

    layout(){
        let parentRect = this.rootDiv.parentElement!.getBoundingClientRect()
        setWidth(this.rootDiv, parentRect.width)
        setHeight(this.rootDiv, parentRect.height)
        for(var i = 0; i < this.contents.length; i++){
            let content = this.contents[i]
            if(content.body instanceof PanelGroup){
                content.body.layout()
            }
        }
    }

    render(): HTMLDivElement{
        if(this.showTitle){
            this.rootDiv.appendChild(this.titleDiv)
            this.titleDiv.appendChild(this.tabView())
            this.titleDiv.appendChild(this.rightDiv)
            this.titleDiv.appendChild(this.rightIcons)
            if(this.contents[this.tabIndex].right){
                this.rightDiv.appendChild(this.contents[this.tabIndex].right!)
            }
            if(this.contents[this.tabIndex].rightIcons){
                this.contents[this.tabIndex].rightIcons!.forEach(i => {
                    const image = document.createElement("div")
                    image.style.backgroundImage = "url(\"" + i.icon + "\")";
                    if(i.onClick){
                        image.onclick = i.onClick
                    }
                    image.title = i.tip || ""
                    this.rightIcons.appendChild(image)
                })
            }
        }
        this.rootDiv.appendChild(this.bodyDiv)
        if(this.showFooter){
            this.rootDiv.appendChild(this.footerDiv)
            this.footerDiv.innerText = this.contents[this.tabIndex].footer || ""
        }
        this.onTabChange(this.tabIndex)
        return this.rootDiv
    }

}


export interface RightIcon{
    icon: string
    tip?: string
    onClick?: ((this: GlobalEventHandlers, ev: MouseEvent) => any)
}
export class PanelContent{
    title: string
    body: HTMLElement | PanelGroup
    footer?: string
    right?: HTMLElement
    rightIcons?: RightIcon[]
    constructor(title: string, body: HTMLElement | PanelGroup, footer?: string, right?: HTMLElement, rightIcons?: RightIcon[]) {
        this.title = title
        this.body = body
        this.footer = footer
        this.right = right
        this.rightIcons = rightIcons
    }

}

export enum Direction{
    Vertical,
    Horizontal
}

let mouseX = 0
let mouseY = 0

document.body.addEventListener("mousemove", e => {
    mouseX = e.clientX
    mouseY = e.clientY
})

interface Rect {
    x: number
    y: number
    width: number
    height: number
}

function isInRect(x: number, y: number, rect: Rect){
    let result = x >= rect.x &&
        x <= rect.x + rect.width &&
        y >= rect.y &&
        y <= rect.y + rect.height
    return result
}
type ResizeCallback = () => void

export class PanelGroup{
    panels: Panel[]
    direction: Direction
    panelDivs: HTMLDivElement[] = []
    sizes: number[] = []
    resizing: boolean = false
    handleSize = 10
    resizingIndex = -1
    resizingStartX = 0
    resizingStartY = 0
    totalSize = 0
    minSize = 100
    rootDiv = createDiv("pl-panel-group")
    dw = 0
    onResize?: ResizeCallback
    constructor(panels: Panel[], direction: Direction, onResize?: ResizeCallback) {
        this.panels = panels;
        this.direction = direction;
        this.onResize = onResize
        this.sizes = panels.map(p => p.size)
        for(let i = 0; i < this.sizes.length; i++){
            if(this.sizes[i] <= 0){
                throw new Error("Invalid size " + this.sizes[i])
            }
            this.totalSize += this.sizes[i]
        }
    }

    getNewSize(): number[]{
        let dw = 0

        return this.sizes.map((p, i) => {
            if(i == this.resizingIndex){
                return p - this.dw
            }else if(i == this.resizingIndex - 1){
                return p + this.dw
            }else {
                return p
            }
        })
    }


    getResizePanelIndex(): number{
        for(let i = 1; i < this.panelDivs.length; i++){
            let div = this.panelDivs[i]
            let domRect = div.getBoundingClientRect()
            let panelRect: Rect = {
                x: domRect.x,
                y: domRect.y,
                width: domRect.width,
                height: domRect.height
            }
            if(!isInRect(mouseX, mouseY, panelRect)){
                continue
            }
            let x = domRect.x
            let y = domRect.y
            let rect: Rect = {
                x: x - 1,
                y: y - 1,
                width: 0,
                height: 0
            }
            if(this.direction == Direction.Vertical){
                rect.width = domRect.width
                rect.height = this.handleSize
            }else if(this.direction == Direction.Horizontal){
                rect.width = this.handleSize
                rect.height = domRect.height
            }

            let isOnHandle = isInRect(mouseX, mouseY, rect)
            if(isOnHandle) {
                return i;
            }
        }
        return -1;
    }

    onMouseMove(){
        let resizePanelIndex = this.getResizePanelIndex()
        if(resizePanelIndex >= 0){
            addClass(this.rootDiv, "hover")
        }else {
            removeClass(this.rootDiv, "hover")
        }
    }

    onMouseDown(){
        let resizePanelIndex = this.getResizePanelIndex()
        this.resizing = resizePanelIndex >= 0
        if(this.resizing){
            addClass(this.panelDivs[resizePanelIndex], "hover")
            this.resizingIndex = resizePanelIndex
            this.resizingStartX = mouseX
            this.resizingStartY = mouseY
            window.requestAnimationFrame(this.animate.bind(this))
        }

    }

    onMouseUp(){
        if(this.resizing){
            this.sizes[this.resizingIndex] -= this.dw
            this.sizes[this.resizingIndex -1] += this.dw
        }
        this.resizing = false
        this.dw = 0
    }

    animate(){
        if(this.resizing){
            if(this.direction == Direction.Horizontal){
                this.dw = this.totalSize * (mouseX - this.resizingStartX)/window.innerWidth
            }else if(this.direction == Direction.Vertical) {
                this.dw = this.totalSize * (mouseY - this.resizingStartY)/window.innerHeight
            }
            this.layout()
            if(this.onResize){
                this.onResize()
            }
            window.requestAnimationFrame(this.animate.bind(this))
        }
    }

    layout(){
        if(!this.rootDiv.parentElement){
            return
        }
        let rect = this.rootDiv.parentElement!.getBoundingClientRect();
        let parentWidth = rect.width
        let parentHeight = rect.height
        let newSizes = this.getNewSize();
        for(let i = 0; i < this.panelDivs.length; i++){
            let div = this.panelDivs[i];
            if(this.direction == Direction.Vertical){
                setWidth(div, parentWidth)
                setHeight(div, Math.max(parentHeight * newSizes[i]/this.totalSize, this.minSize))
            }else if(this.direction == Direction.Horizontal){
                setWidth(div, Math.max(parentWidth * newSizes[i]/this.totalSize, this.minSize))
                setHeight(div, parentHeight)
            }
        }
        for(let i = 0; i < this.panels.length; i++){
            this.panels[i].layout()
        }
    }

    render(): HTMLDivElement{

        if(this.direction == Direction.Vertical){
            addClass(this.rootDiv, "vertical")
        }else if(this.direction == Direction.Horizontal) {
            addClass(this.rootDiv, "horizontal")
        }
        for(var i = 0; i < this.panels.length; i++){
            let content = this.panels[i].render()
            let panelDiv = createDiv("pl-wrapper")
            panelDiv.appendChild(content)
            this.panelDivs.push(panelDiv)
            this.rootDiv.appendChild(panelDiv)
        }
        this.rootDiv.addEventListener("mousemove", this.onMouseMove.bind(this))
        this.rootDiv.addEventListener("mousedown", this.onMouseDown.bind(this))
        this.rootDiv.addEventListener("mouseup", this.onMouseUp.bind(this))
        return this.rootDiv;
    }
}