import React, {useEffect, useState} from "react"
import {ApiClient, CompileResult, CompilerInfo} from "../../api/ApiClient"
import {Editor} from './Editor'
import {Button, ButtonGroup, Divider, Menu, MenuDivider, MenuItem} from "@blueprintjs/core"
import {ItemRenderer} from "@blueprintjs/select";
import {AppWindow} from "../window/AppWindow";
import {Popover2} from "@blueprintjs/popover2";

export interface EditorWindowProps{
    compilers: CompilerInfo[]
    selectLine?: number
    onCompile?: (result: CompileResult) => void
    onSelectLines?: (lines: number[]) => void
}
const selectMenuItemRender: ItemRenderer<CompilerInfo> = (compilerInfo, { handleClick, handleFocus, modifiers, query }) => {
    if (!modifiers.matchesPredicate) {
        return null;
    }
    return (
        <MenuItem
            active={modifiers.active}
            disabled={modifiers.disabled}
            key={compilerInfo.lang + compilerInfo.name}
            text={compilerInfo.name}
            onClick={handleClick}
            onFocus={handleFocus}
            roleStructure="listoption"
            label={`${compilerInfo.lang}`}
        />
    );
};
export const EditorWindow = (props: EditorWindowProps) => {
    const apiClient = ApiClient.getClient()
    const [content, setContent] = useState("")
    const [compilerInfo, setCompilerInfo] = useState(props.compilers[0])
    const [compiling, setCompiling] = useState(false)
    const [debug, setDebug] = useState(false)
    const [optimize, setOptimize] = useState(false)
    const handleEditorContentChange = (content: string) => {
        setContent(content)
    }
    const compile = async () => {
        setCompiling(true)
        try{
            const result = await apiClient.compile(compilerInfo.name, "", [{
                path: compilerInfo.fileName,
                content: content || compilerInfo.example
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
    const isDefaultContent = () => props.compilers.findIndex((c, i) => {
        return !content || c.example == content
    }) > -1
    const handleCompilerChange = (compilerInfo: CompilerInfo) => {
        setCompilerInfo(compilerInfo)
        if(isDefaultContent()){
            compile()
        }
    }
    useEffect(() => {
        compile()
    }, [])
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
                                        .map(lang => <MenuItem text={lang}>
                                            {
                                                props.compilers
                                                    .filter(cl => cl.lang == lang)
                                                    .map(cl => <MenuItem onClick={() => handleCompilerChange(cl)} text={cl.name} />)
                                            }
                                        </MenuItem>)
                                }
                            </Menu>
                        }
                    >
                        <Button minimal={true} text={compilerInfo.name} rightIcon="double-caret-vertical" />
                    </Popover2>
                    <Button minimal={true} active={debug} onClick={() => setDebug(!debug)} title={"Toggle debug"}  icon="bug" />
                    <Button minimal={true} active={optimize} onClick={() => setOptimize(!optimize)} title={"Toggle optimization"}  icon="lightning" />
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
                                <MenuItem text="Perment Link..." />
                                <MenuDivider />
                                <MenuItem text="Self Destruct Link">
                                    <MenuItem text="1 Hour Link..." />
                                    <MenuItem text="1 Day Link.." />
                                    <MenuItem text="7 Days Link..." />
                                </MenuItem>

                            </Menu>
                        }
                    >
                        <Button title={"Share"}  icon="link" />
                    </Popover2>
                    <Popover2
                        autoFocus={false}
                        minimal={true}
                        position="bottom-left"
                        content={
                            <Menu>
                                 <MenuItem text="About..." />
                                 <MenuItem text="Clear Local Cache..." />
                                 <MenuItem text="Privacy Policy..." />
                                 <MenuItem text="Cookie Policy..." />
                            </Menu>
                        }
                    >
                    <Button title={"Main menu"}  icon="menu" />
                    </Popover2>
                </ButtonGroup>
            </div>

    return <AppWindow title={compilerInfo.fileName} actions={titleBarActions}>
        <Editor
            selectLine={props.selectLine}
            onSelectLines={props.onSelectLines}
            onContentChange={handleEditorContentChange}
            lang={compilerInfo.lang}
            content={isDefaultContent() || !content ? compilerInfo.example : undefined}
        />
    </AppWindow>

}