export function addClass(ele: HTMLElement, className: string){
    if(!ele.className.split(" ").includes(className)){
        ele.className = ele.className + " " + className;
    }
}

export function removeClass(ele: HTMLElement, className: string){
    ele.className = ele.className.replace(new RegExp("\\s*" + className), "")
}

export function createDiv(className: string): HTMLDivElement{
    let div = document.createElement("div")
    div.className = className
    return div
}

export function setHeight(ele: HTMLElement, height: number){
    ele.style.height = height + "px"
}

export function setWidth(ele: HTMLElement, height: number){
    ele.style.width = height + "px"
}
