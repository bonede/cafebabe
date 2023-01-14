import './panel.css'
import React, {MouseEventHandler, MutableRefObject, ReactElement, ReactNode, useRef, useState} from "react";

export interface PanelProp{
    children: ReactElement<PanelTabProps, string>[] | ReactElement<PanelTabProps, string>
    showTitle: boolean
    showFooter: boolean
    size: number
    right?: ReactNode
    rightIcons?: RightIcon[]
    minWidth?: number
    minHeight?: number
}


export const Panel = (props: PanelProp) => {
    const children = Array.isArray(props.children) ? props.children : [props.children]
    const [tabIndex, setTabIndex] = useState(0)

    const onTabChange =(index: number) => {
        setTabIndex(index)
    }

    const onTabClick = (index: number) => {
        onTabChange(index)
    }
    const tabView = () => {
        return <div className={(children.length > 1 ? "tab " : "") + "pl-tab"}>
            {
                children.map((t, i) => {
                    return <div onClick={() => onTabClick(i)} className={(tabIndex == i ? "current " : "") + "pl-tab-item"}>
                        {t.props.children}
                    </div>
                })
            }
        </div>
    }


    return <div className="pl-panel" style={{
        minWidth: props.minWidth ? props.minWidth + "px" : "auto",
        minHeight: props.minHeight ? props.minHeight + "px" : "auto"
    }}>
        {
            props.showTitle ? <div className="pl-panel-title">
                {tabView()}
                <div className="pl-panel-right">
                    {props.right}
                </div>
                <div className="pl-panel-right-icons">
                    {props.rightIcons?.map((icon) => {
                        return <div title={icon.tip} onClick={icon.onClick} style={{backgroundImage: "url(\"" + icon.icon + "\")"}}></div>
                    })}
                </div>
            </div> : null
        }
        <div className={"pl-body"}>{
            children[tabIndex].props?.children
        }
        </div>
        {
            props.showFooter ? <div className="pl-panel-footer">
                {children[tabIndex].props?.footer}
            </div> : null
        }
    </div>

}


export interface RightIcon{
    icon: string
    tip?: string
    onClick?: MouseEventHandler
}
export interface PanelTabProps{
    title?: string
    children: ReactNode
    footer?: string
}

export const PanelTab: React.FC<PanelTabProps> = (props) => {
    return <>123</>
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


export interface PanelGroupProps{
    children: ReactNode,
    direction: Direction,
    sizes: number[]
    resizing?: boolean,
    onResize?: ResizeCallback
}

export function PanelGroup(props: PanelGroupProps){
    const [resizingPanelIndex, setResizingPanelIndex] = useState(-1)
    const panelGroupDiv = useRef() as MutableRefObject<HTMLDivElement>
    var handleSize = 10

    var resizingStartX = 0
    var resizingStartY = 0
    var totalSize = 0
    var minSize = 100
    var dw = 0
    var isResizing = false


    const getNewSizes = () : number[] => {
        let dw = 0
        return props.sizes.map((p, i) => {
            if(i == resizingPanelIndex){
                return p - dw
            }else if(i == resizingPanelIndex - 1){
                return p + dw
            }else {
                return p
            }
        })
    }

    const getResizePanelIndex = () : number => {
        let panelDivs = panelGroupDiv.current.getElementsByClassName("panel")
        for(let i = 1; i < panelDivs.length; i++){
            let div = panelDivs[i]
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
            if(props.direction == Direction.Vertical){
                rect.width = domRect.width
                rect.height = handleSize
            }else if(props.direction == Direction.Horizontal){
                rect.width = handleSize
                rect.height = domRect.height
            }

            let isOnHandle = isInRect(mouseX, mouseY, rect)
            if(isOnHandle) {
                return i;
            }
        }
        return -1;
    }

    const onMouseMove = () => {
        let resizePanelIndex = getResizePanelIndex()
        if(resizePanelIndex >= 0){
            setResizingPanelIndex(resizePanelIndex)
        }else {
            setResizingPanelIndex(resizePanelIndex)
        }
    }

    const onMouseDown = () => {
        let resizePanelIndex = getResizePanelIndex()
        if(resizePanelIndex >= 0){
            setResizingPanelIndex(resizePanelIndex)
            resizingStartX = mouseX
            resizingStartY = mouseY
            window.requestAnimationFrame(animate)
        }

    }

    const onMouseUp = () => {
        if(isResizing){
            props.sizes[resizingPanelIndex] -= dw
            props.sizes[resizingPanelIndex -1] += dw
        }
        isResizing = false
        dw = 0
    }

    const animate = () => {
        if(props.resizing){
            if(props.direction == Direction.Horizontal){
                dw = totalSize * (mouseX - resizingStartX)/window.innerWidth
            }else if(props.direction == Direction.Vertical) {
                dw = totalSize * (mouseY - resizingStartY)/window.innerHeight
            }
            if(props.onResize){
                props.onResize()
            }
            window.requestAnimationFrame(animate)
        }
    }

    // layout(){
    //     if(!this.rootDiv.parentElement){
    //         return
    //     }
    //     let rect = this.rootDiv.parentElement!.getBoundingClientRect();
    //     let parentWidth = rect.width
    //     let parentHeight = rect.height
    //     let newSizes = this.getNewSize();
    //     for(let i = 0; i < this.panelDivs.length; i++){
    //         let div = this.panelDivs[i];
    //         if(this.direction == Direction.Vertical){
    //             setWidth(div, parentWidth)
    //             setHeight(div, Math.max(parentHeight * newSizes[i]/this.totalSize, this.minSize))
    //         }else if(this.direction == Direction.Horizontal){
    //             setWidth(div, Math.max(parentWidth * newSizes[i]/this.totalSize, this.minSize))
    //             setHeight(div, parentHeight)
    //         }
    //     }
    //     for(let i = 0; i < this.panels.length; i++){
    //         this.panels[i].layout()
    //     }
    // }

    return <div onMouseDown={onMouseDown} onMouseUp={onMouseUp} onMouseMove={onMouseMove} ref={panelGroupDiv} className={(props.direction == Direction.Vertical ?  "vertical" : "horizontal") + " pl-panel-group"}>
        {props.children}
    </div>
}