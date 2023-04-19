import React, {useEffect, useState} from "react"
import {ApiClient, CompileResult, CompilerInfo} from "../../api/ApiClient"
import {Editor} from './Editor'
import {Button, ButtonGroup, MenuItem} from "@blueprintjs/core"
import {ItemRenderer, Select2} from "@blueprintjs/select";
import {AppWindow} from "../window/AppWindow";

export interface EditorWindowProps{
    compilers: CompilerInfo[]
    selectLine?: number
    onCompile?: (result: CompileResult) => void
    onSelectLines?: (lines: number[]) => void
}
const selectMenuItem: ItemRenderer<CompilerInfo> = (compilerInfo, { handleClick, handleFocus, modifiers, query }) => {
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
    const handleCompilerChange = (compilerInfo: CompilerInfo) => {
        setCompilerInfo(compilerInfo)
        compile()
    }
    useEffect(() => {
        compile()
    }, [])
    const  titleBarActions=
            <div style={{display: "flex"}}>
                <Select2<CompilerInfo>
                    filterable={false}
                    items={props.compilers}
                    onItemSelect={handleCompilerChange}
                    itemRenderer={selectMenuItem}
                >
                    <Button minimal={true} text={compilerInfo.name} rightIcon="double-caret-vertical" placeholder="Select a compiler" />
                </Select2>
                <ButtonGroup minimal={true}>
                    <Button title={"Build"} onClick={handleCompileClick} icon="build" loading={compiling} />
                    <Button title={"Share"}  icon="link" />
                </ButtonGroup>
            </div>

    return <AppWindow title={compilerInfo.fileName} actions={titleBarActions}>
        <Editor
            selectLine={props.selectLine}
            onSelectLines={props.onSelectLines}
            onContentChange={handleEditorContentChange}
            lang={compilerInfo.lang}
            content={compilerInfo.example}
        />
    </AppWindow>

}