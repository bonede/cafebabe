import React, {useEffect, useRef, useState} from "react"
import {ApiClient, AppState, Compiler, CompileResult, CompilerOps, PubShareFile, SrcFile} from "../../api/ApiClient"
import {Editor, EditorRef} from './Editor'
import {Button, ButtonGroup, Divider, Menu, MenuDivider, MenuItem} from "@blueprintjs/core"
import {AppWindow} from "../window/AppWindow";
import {Popover2} from "@blueprintjs/popover2";
import {getShareId, showToast} from "../Utils";

export interface EditorWindowProps{
    compilers: Compiler[]
    selectLine?: number
    onCompile?: (result: CompileResult) => void
    onSelectLines?: (lines: number[]) => void
    onShareReq?: (ops: CompilerOps, srcFiles: SrcFile[], hoursToLive?: number) => Promise<void>
    onDeleteReq?: () => void
}

export const EditorWindow = (props: EditorWindowProps) => {
    const apiClient = ApiClient.getClient()
    const [compiler, setCompiler] = useState(props.compilers[0])
    const [compiling, setCompiling] = useState(false)
    const [sharing, setSharing] = useState(false)
    const [debug, setDebug] = useState(false)
    const [loading, setLoading] = useState(true)
    const [shareFile, setShareFile] = useState(undefined as PubShareFile | undefined)
    const [optimize, setOptimize] = useState(false)
    const [appState, setAppState] = useState({} as AppState)
    const editorRef = useRef(null as EditorRef | null);
    const getEditorContent = (): string | undefined => {
        return editorRef.current?.getContent()
    }

    const setEditorContent = (content: string) => {
        editorRef.current?.setContent(content)
    }

    const initData = async () => {
        const localAppState = apiClient.getAppState()
        if(localAppState !== null){
            appState.compiler = localAppState.compiler
            appState.optimize = localAppState.optimize
            appState.debug = localAppState.debug
            appState.editorContent = localAppState.editorContent
        }
        const shareId = getShareId(window.location.href)
        if(shareId){
            setLoading(true)
            try {
                const shareFile = await apiClient.getShare(shareId)
                setShareFile(shareFile)
                setCompiler(props.compilers.filter(c => c.name == shareFile.ops.compilerName)[0])
                setDebug(shareFile.ops.debug)
                setOptimize(shareFile.ops.optimize)
                setLoading(false)
            }catch (e){
                showToast(e + "", "danger")
                setLoading(false)
            }
        }else {
            setLoading(false)
        }
        return appState
    }



    const compile = async (c?: Compiler) => {
        if(!getEditorContent()){
            showToast("Editor content is empty", "danger")
            return
        }
        setCompiling(true)
        const com = c || compiler
        try{
            console.log("compiler", c)
            const ops: CompilerOps = {
                compilerName: com.name,
                debug,
                optimize
            }
            const result = await apiClient.compile(ops, [{
                path: com.fileName,
                content: getEditorContent()!
            }])
            props.onCompile && props.onCompile(result)
            setCompiling(false)
        }catch (e){
            props.onCompile && props.onCompile({
                classFiles: [],
                compiler: "",
                compilerOptions: "",
                returnCode: -1,
                stderr: e + "",
                stdout: "",
            })
            setCompiling(false)
        }
    }

    const handleCompileClick = async () => {
        return compile()
    }

    const handleCompilerChange = (compiler: Compiler) => {
        setCompiler(compiler)
        appState.compiler = compiler
        apiClient.saveAppState(appState)
    }
    useEffect(() => {
        initData().then((appState) => setTimeout(() => compile(appState.compiler), 0.5 * 1000))
    }, [])

    const handleShareClick = async (hoursToLive?: number) => {
        if (!props.onShareReq) {
            return
        }
        if(!getEditorContent()){
            showToast("Editor content is empty", "danger")
        }
        try {
            setSharing(true)
            await props.onShareReq({
                compilerName: compiler.name,
                debug,
                optimize
            }, [{
                content: getEditorContent()!,
                path: compiler.fileName
            }], hoursToLive)
            setSharing(false)
        } catch (e) {
            setSharing(false)
        }
    }
    const handleDebugClick = () => {
        appState.debug = !debug
        setDebug(appState.debug)
        apiClient.saveAppState(appState)
    }

    const handleOptimizeClick = () => {
        appState.optimize = !optimize
        setOptimize(appState.optimize)
        apiClient.saveAppState(appState)
    }

    const  titleBarActions=
            <div style={{display: "flex"}}>
                <ButtonGroup>
                    <Popover2
                        autoFocus={false}
                        minimal={true}
                        position="bottom-left"
                        content={
                            <Menu>
                                {
                                    props.compilers
                                        .map(c => c.lang)
                                        .reduce((acc: string[], c: string, i) => {
                                            if(acc.includes(c)){
                                                return acc
                                            }else {
                                                acc.push(c)
                                            }
                                            return acc
                                        }, [])
                                        .map(lang => <MenuItem key={lang} text={lang}>
                                            {
                                                props.compilers
                                                    .filter(cl => cl.lang == lang)
                                                    .map(cl => <MenuItem key={cl.name} onClick={() => handleCompilerChange(cl)} text={cl.name} />)
                                            }
                                        </MenuItem>)
                                }
                            </Menu>
                        }
                    >
                        <Button minimal={true} text={compiler.name} rightIcon="double-caret-vertical" />
                    </Popover2>
                    <Button minimal={true} active={debug} onClick={handleDebugClick} title={"Toggle Debug"}  icon="bug" />
                    <Button minimal={true} active={optimize} onClick={handleOptimizeClick} title={"Toggle Optimization"}  icon="lightning" />
                </ButtonGroup>

                <Divider />
                <ButtonGroup minimal={true}>
                    <Button title={"Build"} onClick={handleCompileClick} icon="build" loading={compiling} />
                    <Popover2
                        autoFocus={false}
                        minimal={true}
                        position="bottom-left"
                        content={
                            <Menu>
                                <MenuItem onClick={() => handleShareClick()} text="Permanent Link..." />
                                <MenuDivider />
                                <MenuItem text="Self Destruct Link">
                                    <MenuItem onClick={() => handleShareClick(1)}  text="1 Hour Link..." />
                                    <MenuItem onClick={() => handleShareClick(24)} text="1 Day Link.." />
                                    <MenuItem onClick={() => handleShareClick( 7 * 24)} text="7 Days Link..." />
                                </MenuItem>

                            </Menu>
                        }
                    >
                        <Button loading={sharing} title={"Share"}  icon="link" />
                    </Popover2>
                    <Popover2
                        autoFocus={false}
                        minimal={true}
                        position="bottom-left"
                        content={
                            <Menu>
                                <MenuItem text="File">
                                    <MenuItem text="Open Exmaples">
                                        {
                                            props.compilers.map(c => <MenuItem key={c.name} text={c.name} onClick={() => setEditorContent(c.example)} />)
                                        }
                                    </MenuItem>
                                </MenuItem>
                                <MenuItem text="Help">
                                    <MenuItem text="About..." />
                                    <MenuItem text="Clear Local Cache..." />
                                    <MenuItem text="Privacy Policy..." />
                                    <MenuItem text="Cookie Policy..." />
                                    <MenuItem onClick={props.onDeleteReq} text="Delete Share Link..." />
                                </MenuItem>
                            </Menu>
                        }
                    >
                    <Button title={"Main menu"}  icon="menu" />
                    </Popover2>
                </ButtonGroup>
            </div>

    const getInitContent = () => {
        if(shareFile){
            return shareFile.srcFiles[0].content
        }else if(appState.editorContent){
            return appState.editorContent
        }else {
            return compiler.example
        }
    }


    setInterval(() => {
        if(getEditorContent()){
            appState.editorContent = getEditorContent()
            apiClient.saveAppState(appState)
        }
    }, 3000)


    return <AppWindow title={compiler.fileName} actions={titleBarActions}>
        {
            loading ? <div></div> : <div className="editor-window-content"><Editor
                ref={editorRef}
                selectLine={props.selectLine}
                onSelectLines={props.onSelectLines}
                lang={compiler.lang}
                initContent={getInitContent()}
            /></div>
        }

    </AppWindow>

}