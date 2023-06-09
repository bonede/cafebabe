import React, {useEffect, useRef, useState} from "react"
import {ApiClient, Compiler, CompileResult, CompilerOps, EditorState, PubShareFile, SrcFile} from "../../api/ApiClient"
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
    onDeleteReq?: () => void,
    onAboutClick?: () => void
    onClearCacheClick?: () => void
    onPrivacyPolicyClick?: () => void
    onCookiePolicyClick?: () => void
}

export const EditorWindow = (props: EditorWindowProps) => {
    const apiClient = ApiClient.getClient()
    const [compiling, setCompiling] = useState(false)
    const [uploading, setUploading] = useState(false)
    const [sharing, setSharing] = useState(false)
    const [loading, setLoading] = useState(true)
    const [shareFile, setShareFile] = useState(undefined as PubShareFile | undefined)
    const [editorState, setEditorState] = useState({
        optimize: true,
        debug: true,
        compiler: props.compilers[0],
        editorContent: ""
    } as EditorState)
    const editorRef = useRef(null as EditorRef | null);
    const getEditorContent = (): string | undefined => {
        return editorRef.current?.getContent()
    }

    const setEditorContent = (content: string) => {
        editorRef.current?.setContent(content)
    }

    const initData = async () => {
        const localAppState = apiClient.getEditorState()
        if(localAppState !== null){
            editorState.compiler = localAppState.compiler
            editorState.optimize = localAppState.optimize
            editorState.debug = localAppState.debug
            editorState.editorContent = localAppState.editorContent
            if(localAppState.compiler){
                editorState.compiler = localAppState.compiler
            }
        }
        const shareId = getShareId(window.location.href)
        if(shareId){
            setLoading(true)
            try {
                const shareFile = await apiClient.getShare(shareId)
                setShareFile(shareFile)
                editorState.compiler = props.compilers.filter(c => c.name == shareFile.ops.compilerName)[0]
                editorState.debug = shareFile.ops.debug
                editorState.optimize = shareFile.ops.optimize
                setLoading(false)
            }catch (e){
                showToast(e + "", "danger")
                setLoading(false)
            }
        }else {
            setLoading(false)
        }
        return editorState
    }

    const compile = async (c?: Compiler) => {
        window.gtag && window.gtag("event", "compile")
        if(!getEditorContent()){
            showToast("Editor content is empty", "danger")
            return
        }
        setCompiling(true)
        const com = c || editorState.compiler
        try{
            const ops: CompilerOps = {
                compilerName: com.name,
                debug: editorState.debug,
                optimize: editorState.optimize
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

    const handleUploadClick = () => {

    }

    const handleCompilerChange = (compiler: Compiler) => {
        editorState.compiler = compiler
        setEditorState({...editorState})
        apiClient.saveAppState(editorState)
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
                compilerName: editorState.compiler.name,
                debug: editorState.debug,
                optimize: editorState.optimize
            }, [{
                content: getEditorContent()!,
                path: editorState.compiler.fileName
            }], hoursToLive)
            setSharing(false)
        } catch (e) {
            setSharing(false)
        }
    }

    const handleDebugClick = () => {
        editorState.debug = !editorState.debug
        setEditorState({...editorState})
        apiClient.saveAppState(editorState)
    }

    const handleOptimizeClick = () => {
        editorState.optimize = !editorState.optimize
        setEditorState({...editorState})
        apiClient.saveAppState(editorState)
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
                        <Button minimal={true} text={editorState.compiler.name} rightIcon="double-caret-vertical" />
                    </Popover2>
                    <Button className={'toggle'} minimal={true} active={editorState.debug} onClick={handleDebugClick} title={"Toggle compiler debug flag"}  icon="bug" />
                    <Button className={'toggle'} minimal={true} active={editorState.optimize} onClick={handleOptimizeClick} title={"Toggle compiler optimize flag"}  icon="lightning" />
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
                                    <MenuItem onClick={props.onAboutClick} text="About" />
                                    <MenuItem onClick={props.onClearCacheClick} text="Clear Local Cache" />
                                    <MenuItem onClick={props.onPrivacyPolicyClick} text="Privacy Policy" />
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
        }else if(editorState.editorContent){
            return editorState.editorContent
        }else {
            return editorState.compiler.example
        }
    }


    setInterval(() => {
        if(getEditorContent()){
            editorState.editorContent = getEditorContent() || ""
            apiClient.saveAppState(editorState)
        }
    }, 3000)


    return <AppWindow title={editorState.compiler.fileName} actions={titleBarActions}>
        {
            loading ? <div></div> : <div className="editor-window-content"><Editor
                ref={editorRef}
                selectLine={props.selectLine}
                onSelectLines={props.onSelectLines}
                lang={editorState.compiler.lang}
                initContent={getInitContent()}
            /></div>
        }

    </AppWindow>

}