import './panel.css'
/*

             root
     left           right
                    panel
top     bottom
panel   panel



 */
import React, {CSSProperties, useRef, useState} from "react";

type orientation = "vertical" | "horizontal"
export interface PanelNode {
    flex?: boolean
    width?: number
    height?: number
    minWidth?: number
    minHeight?: number
    orientation?: orientation
    children: JSX.Element | PanelNode[]
}

export interface PanelRootProp{
    child: PanelNode
    header: JSX.Element
    footer: JSX.Element
}
function panelFooter(props: PanelRootProp){
    return <div className="pl-footer">{props.footer}</div>
}

function panelHeader(props: PanelRootProp){
    return <div className="pl-header">{props.header}</div>
}

interface PanelGroupProps{
    nodes: PanelNode[]
    orientation: orientation
}

function PanelGroup(props: PanelGroupProps){
    let onMouseMove = (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        let target = e.target
    }
    return <div
        onMouseMove={onMouseMove}
        className={`pl-panel-group ${props.orientation}`} style={{
        gridTemplateColumns: "1fr 1fr 1fr"
    }
    }>
        {props.nodes.map((n,i) => <Panel index={i} node={n} />)}
    </div>
}

interface PanelProp{
    node: PanelNode
    index: number
}
let resizeRegion = 5
let isMinimal = false
function isAboveMinimal(minWidth: number, minHeight: number, div: HTMLDivElement){
    let rect = div.getBoundingClientRect();
    let targetWidth = rect.width
    let targetHeight = rect.height
    return targetWidth > minWidth && targetHeight > minHeight
}
function isInResizeRegion(mouseX: number, mouseY: number, div: HTMLDivElement, orientation: orientation): boolean{
    let rect = div.getBoundingClientRect();
    let targetX = rect.x
    let targetY = rect.y
    let targetWidth = rect.width
    let targetHeight = rect.height

    if(orientation == 'vertical'){
        return mouseX >= targetX && mouseX <= targetX + targetWidth &&
            mouseY >= targetY && mouseY <= targetY + resizeRegion
    }else{
        return mouseX >= targetX && mouseX <= targetX + resizeRegion &&
            mouseY >= targetY && mouseY <= targetY + targetHeight
    }
}
let mouseX = 0
let mouseY = 0
let mouseDownX = 0
let mouseDownY = 0
let resizing = false

document.addEventListener('mousemove', e => {
    mouseX = e.clientX
    mouseY = e.clientY
})

document.addEventListener('mouseup', () => {
    resizing = false
})

function Panel(prop: PanelProp){
    let node = prop.node
    let divRef = useRef(null)
    let [onResizeEdge, setOnResizeEdge] = useState(false)
    let [width, setWidth] = useState(prop.node.width)
    let [height, setHeight] = useState(prop.node.height)
    let minWidth = prop.node.minWidth ? prop.node.minWidth : 50
    let minHeight = prop.node.minHeight ? prop.node.minHeight : 50
    if(Array.isArray(node.children)){
        return <PanelGroup nodes={node.children} orientation={prop.node.orientation!} />
    }else {
        let flex = node.flex ? "flex" : ""
        let style: CSSProperties = {}
        let resize = onResizeEdge ? "resize-edge" : ""

        let animation = () => {
            let deltaX = mouseX - mouseDownX
            let deltaY = mouseY - mouseDownY
            let w = width! - deltaX
            if(w < minWidth){
                w = minWidth
            }
            setWidth(w)
            setHeight(height! - deltaY)
            if(resizing){
                window.requestAnimationFrame(animation)
            }

        }
        let onMouseDown = (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
            let div = divRef.current! as HTMLDivElement
            let isResize = isInResizeRegion(e.clientX, e.clientY, div, node.orientation!)
            if(!isResize){
                return
            }
            resizing = true
            mouseDownX = mouseX
            mouseDownY = mouseY
            window.requestAnimationFrame(animation)
        }

        return <div
            key={prop.index}
            ref={divRef}

            className={`pl-panel ${flex} ${resize}`} style={style}
        >
                {node.children}
        </div>
    }
}

function panelBody(props: PanelRootProp){
    return <div className="pl-body">
        <Panel index={0} node={props.child} />
    </div>
}
export function PanelRoot(props: PanelRootProp){
    return <div className="pl-root">
        {panelHeader(props)}
        {panelBody(props)}
        {panelFooter(props)}
    </div>
}

