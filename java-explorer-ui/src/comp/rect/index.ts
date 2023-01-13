import {Direction} from "../panel";

export interface Rect {
    x: number
    y: number
    width: number
    height: number
}

export function isInRect(x: number, y: number, rect: Rect){
    let result = x >= rect.x &&
        x <= rect.x + rect.width &&
        y >= rect.y &&
        y <= rect.y + rect.height
    return result
}
const RESIZE_HANDLE_BAR_SIZE = 10

export function getPanelElement(e: MouseEvent): HTMLElement | null{
    let ele = e.target as HTMLElement | null;
    while (ele){
        if(ele.className && ele.className.split(" ").includes("pl-panel")){
            return ele
        }
        ele = ele.parentElement
    }
    return null
}

export function isMouseOnHandleBar(e: MouseEvent): boolean{
    const thisPanelDiv = getPanelElement(e)
    if(!thisPanelDiv){
        return false
    }
    const panelGroupDiv = thisPanelDiv.parentElement!;
    const panelDivs= panelGroupDiv.children;
    if(panelDivs.length == 1){
        return false;
    }
    if(thisPanelDiv == panelDivs[panelDivs.length - 1]){
        return false;
    }
    const direction = panelGroupDiv.className.includes("vertical") ? Direction.Vertical : Direction.Horizontal;
    const x = e.clientX;
    const y = e.clientY;

    const handleBarRect : Rect = direction == Direction.Horizontal ? {
        x: thisPanelDiv.offsetLeft + thisPanelDiv.offsetWidth - RESIZE_HANDLE_BAR_SIZE,
        y: thisPanelDiv.offsetTop,
        width: RESIZE_HANDLE_BAR_SIZE,
        height: thisPanelDiv.offsetHeight
    } : {
        x: thisPanelDiv.offsetLeft,
        y: thisPanelDiv.offsetTop + thisPanelDiv.offsetHeight - RESIZE_HANDLE_BAR_SIZE ,
        width: thisPanelDiv.offsetWidth,
        height: RESIZE_HANDLE_BAR_SIZE
    };
    console.warn(thisPanelDiv.offsetHeight)
    return isInRect(x, y , handleBarRect);
}
