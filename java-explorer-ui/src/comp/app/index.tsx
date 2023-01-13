import './index.css'
import {Direction, Panel, PanelGroup, PanelTab} from "../panel";
import * as monaco from "monaco-editor";
import React, {useEffect} from "react";
import {getPanelElement, isMouseOnHandleBar} from "../rect";
import {addClass, removeClass} from "../Utils";


export function JavaExplorerApp(){
    // editor
    // editorDiv: HTMLDivElement
    // loggerDiv: HTMLDivElement
    // logger: Logger
    // rootPanel: Panel
    const onResize = () => {

    }
    var resizing = false
    var startX = 0
    var startY = 0
    var startFlex = 0
    var panelEle = null as HTMLElement | null

    const resize = (ele: HTMLElement, x: number, y: number) =>{
        const sibling = ele.nextElementSibling as HTMLDivElement
        console.log("resizing")
        const panelGroupEle = ele.parentElement!
        const panelGroupWidth = panelGroupEle.offsetWidth
        const direction = panelGroupEle.className.includes("vertical") ? Direction.Vertical : Direction.Horizontal;
        const deltaX = x - startX
        const deltaY = y - startY
        const delta  = direction == Direction.Horizontal ? deltaX : deltaY
        const deltaFlex = 2 * delta/panelGroupWidth

        ele.style.flexBasis = "0";
        ele.style.flexShrink = "0";
        const flex = startFlex + deltaFlex
        ele.style.flexGrow = ( startFlex + deltaFlex).toString();

        sibling.style.flexBasis = "0";
        sibling.style.flexShrink = "0";
        sibling.style.flexGrow = ( 2 - flex).toString();
        console.log(flex)
    }

    const handleWindowMouseMove = (e: MouseEvent) => {
        if(resizing){
            resize(panelEle!, e.clientX, e.clientY)
            return
        }
        const showHandleBar = isMouseOnHandleBar(e);
        const thisPanelEle = getPanelElement(e)
        if(showHandleBar){
            addClass(thisPanelEle!, "hover")
        }else {
            removeClass(thisPanelEle!, "hover")
        }

    }

    const handleWindowMouseDown = (e: MouseEvent) => {
        if(isMouseOnHandleBar(e)){
            resizing = true;
            panelEle = getPanelElement(e)!
            startX = e.clientX
            startY = e.clientY
            console.warn("BEGIN")
            startFlex = parseFloat(getComputedStyle(getPanelElement(e)!).flexGrow)
        }
    }

    const handleWindowMouseUp = (e: MouseEvent) => {
        resizing = false;
        panelEle = null
        startX = 0
        startY = 0
        startFlex = 0
    }
    useEffect(()=> {
        window.addEventListener("mousemove", handleWindowMouseMove)
        window.addEventListener("mousedown", handleWindowMouseDown)
        window.addEventListener("mouseup", handleWindowMouseUp)
    }, [])
    //
    // resizeEditor(){
    //     let rect = this.editorDiv.parentElement!.getBoundingClientRect()
    //     this.editor.layout({
    //         width: rect.width,
    //         height: rect.height
    //     })
    // }


    const editorDiv = document.createElement("div")
    editorDiv.className = "editor"
    monaco.editor.defineTheme('vs-dark-enhanced', {
        base: 'vs-dark',
        inherit: true,
        rules: [],
        colors: {
            'editor.foreground': "#bdc1c6",
            'editor.lineHighlightBackground': '#303134',
            'editor.lineHighlightBorder': '#303134',
            "editor.overviewRulerBorder": "false"
        }
    });
    const editor = monaco.editor.create(editorDiv, {
        theme: "vs-dark-enhanced",
        value: ['function x() {', '\tconsole.log("Hello world!");', '}'].join('\n'),
        language: 'java',
        fontSize: 18,
        automaticLayout: false,
        minimap: {enabled: false},
        smoothScrolling: true,
        scrollbar: {
            vertical: 'visible',
            horizontal: 'visible',
            useShadows: false
        }
    })


    const left = <Panel right={<select><option>Java11</option> </select>}
                  rightIcons={
                      [
                          {
                              tip: "Upload",
                              icon: "upload.svg"
                          },
                          {
                              tip: "Share",
                              icon: "share.svg"
                          }
                      ]
                  } showTitle={true} showFooter={true} size={1}>
        <PanelTab

                  footer="12kb" title="Main.java"><div>1122</div></PanelTab>
        <PanelTab footer="12kb" title="Foo.java"><div>1124442</div></PanelTab>
    </Panel>

    const right = <PanelGroup direction={Direction.Vertical} sizes={[1]}>
        <Panel showTitle={true} showFooter={true} size={1}>
            <PanelTab title={"12"}>12</PanelTab>
        </Panel>
        <Panel showTitle={true} showFooter={true} size={1}>
            <PanelTab title={"12"}>12</PanelTab>
        </Panel>
    </PanelGroup>
    return <PanelGroup direction={Direction.Horizontal} sizes={[1]}>
        {left}
        {right}
    </PanelGroup>

}