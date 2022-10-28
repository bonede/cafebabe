import './index.css'
import {Direction, Panel, PanelContent, PanelGroup} from "../panel";
import * as monaco from "monaco-editor";
import {Logger} from "../logger";

export class JavaExplorerApp{
    editor
    editorDiv: HTMLDivElement
    loggerDiv: HTMLDivElement
    logger: Logger
    rootPanel: Panel
    onResize(){
        this.rootPanel.layout()
        this.resizeEditor()
    }

    resizeEditor(){
        let rect = this.editorDiv.parentElement!.getBoundingClientRect()
        this.editor.layout({
            width: rect.width,
            height: rect.height
        })
    }

    public constructor() {
        this.editorDiv = document.createElement("div")
        this.editorDiv.className = "editor"
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
        this.editor = monaco.editor.create(this.editorDiv, {
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
        const versionSelect = document.createElement("select")
        const option = document.createElement("option")
        option.innerText = "java11"
        versionSelect.appendChild(option)
        let codeContent =  new PanelContent(
            "Main.java",
            this.editorDiv,
            undefined,
            versionSelect,
            [
                {
                    icon: "upload.svg",
                    tip: "Upload"
                },
                {
                    icon: "share.svg",
                    tip: "Share"
                }
            ]
        )

        let codePanel = new Panel([codeContent], 1, true, false)
        this.loggerDiv = document.createElement("div")
        this.logger = new Logger(this.loggerDiv)
        let outputContent =  new PanelContent(
            "Output",
            this.loggerDiv,
            "Output Footer",
        )
        let outputPanel = new Panel([outputContent], 1, true, false)

        let leftPanelGroup = new PanelGroup([codePanel, outputPanel], Direction.Vertical, this.onResize.bind(this))

        let leftPanelContent = new PanelContent(
            "Left",
            leftPanelGroup,
            "Left footer"
        )
        let rightA = document.createElement("div")
        rightA.innerText = "Right A"
        let rightB = document.createElement("div")
        rightB.innerText = "Right B"
        let rightPanelContentA = new PanelContent(
            "Main.class",
            rightA,
            "Right footer"
        )
        let rightPanelContentB = new PanelContent(
            "Right B",
            rightB,
            "Right footer"
        )
        let leftPanel = new Panel([leftPanelContent], 1, false, false)
        let rightPanel = new Panel([rightPanelContentA, rightPanelContentB], 1, true, false)
        let mainContentPanelGroup = new PanelGroup(
            [leftPanel, rightPanel],
            Direction.Horizontal,
            this.onResize.bind(this)
        )

        let rootPanelContent = new PanelContent(
            "JavaExplorer",
            mainContentPanelGroup,
            "@copy 2022"
        )
        this.rootPanel = new Panel([rootPanelContent], 1)
        let rootDiv = this.rootPanel.render()
        document.body.appendChild(rootDiv)
        this.rootPanel.layout()
        this.resizeEditor()
        window.addEventListener("resize", this.onResize.bind(this))
        this.loggerDiv.addEventListener("click", () => {
            this.logger.appendLog("info", "Note: the background must be transparent for the border to show up at all. So, alternatively, you can also set the main background color as the highlight background color to make the border disappear.\n" +
                "\n" +
                "Play with the code in the Monaco Editor playground to see the various effects.")
            this.logger.appendLog("error", "Note: the background must be transparent for the border to show up at all. So, alternatively, you can also set the main background color as the highlight background color to make the border disappear.\n" +
                "\n" +
                "Play with the code in the Monaco Editor playground to see the various effects.")
        })
    }
}