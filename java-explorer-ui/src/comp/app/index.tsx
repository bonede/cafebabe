import './index.css'
import {Direction, Panel, PanelGroup, PanelTab} from "../panel";
import * as monaco from "monaco-editor";
import React, {useEffect} from "react";
import {getPanelElement, isMouseOnHandleBar, resize} from "../rect";
import {addClass, removeClass} from "../Utils";


export function JavaExplorerApp(){
    // editor
    // editorDiv: HTMLDivElement
    // loggerDiv: HTMLDivElement
    // logger: Logger
    // rootPanel: Panel
    const onResize = () => {

    }
    let resizing = false
    let startX = 0
    let startY = 0
    let lastX = 0
    let lastY = 0
    let startFlex = 0
    let lastWidth = 0
    let lastHeight = 0
    let panelEle = null as HTMLElement | null




    const handleWindowMouseMove = (e: MouseEvent) => {
        if(resizing){
            resize(panelEle!, e.clientX, e.clientY, startX, startY, lastX, lastY, startFlex)
            lastX = e.clientX
            lastY = e.clientY
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
            lastWidth = panelEle.offsetWidth
            lastHeight = panelEle.offsetHeight
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


    const left = <Panel minWidth={400} right={<select><option>Java11</option> </select>}
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
        <PanelTab footer="12kb" title="Main.java"><div>1122</div></PanelTab>
        <PanelTab footer="36kb" title="Foo.java"><div>1124442</div></PanelTab>
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