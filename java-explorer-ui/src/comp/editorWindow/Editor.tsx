import './Editor.css'
import * as monaco from 'monaco-editor';
import {editor} from 'monaco-editor';
import {useEffect, useRef, useState} from "react";
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;
import IEditorDecorationsCollection = editor.IEditorDecorationsCollection;

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
export interface EditorProps{
    lang: string
    content: string
    onContentChange?: (content: string) => void
    onSelectLines?: (lines: number[]) => void
    selectLine?: number
}
export function Editor(props: EditorProps){
    let [editor, setEditor] = useState<IStandaloneCodeEditor>()
    const editorElement = useRef(null)
    const [lineDecoration, setLineDecoration] = useState(undefined as IEditorDecorationsCollection | undefined)
    useEffect(() => {
        monaco.editor.addKeybindingRules([
            {
                keybinding: monaco.KeyMod.CtrlCmd | monaco.KeyCode.Equal,
                command: "editor.action.fontZoomIn", // ID
                when: "textInputFocus", // When
            },
            {
                keybinding: monaco.KeyMod.CtrlCmd | monaco.KeyCode.Minus,
                command: "editor.action.fontZoomOut", // ID
                when: "textInputFocus", // When
            },
            {
                keybinding: monaco.KeyMod.CtrlCmd | monaco.KeyCode.Digit0,
                command: "editor.action.fontZoomReset", // ID
                when: "textInputFocus", // When
            }
        ]);
        editor = monaco.editor.create(editorElement.current!, {
            theme: "vs-dark-enhanced",
            value: props.content,
            language: props.lang,
            fontSize: 18,
            automaticLayout: false,
            minimap: {enabled: false},
            smoothScrolling: true,
            mouseWheelZoom: true,
            scrollbar: {
                vertical: 'visible',
                horizontal: 'visible',
                useShadows: false
            },
        })

        editor.getModel()?.onDidChangeContent(e => {
            props.onContentChange?.(editor?.getValue() || "")
        })

        editor.onDidChangeCursorSelection((e) => {
            const lines: number[] = []
            for(let i = e.selection.startLineNumber; i <= e.selection.endLineNumber;){
                lines.push(i++)
            }
            props.onSelectLines && props.onSelectLines(lines)
        });

        setEditor(editor)
    }, []);
    const handleContentChange = () => {
        if(props.content && editor){
            editor.setValue(props.content)
            props.onContentChange?.(props.content)
        }
    }

    const handleSelectLineChange = () => {
        lineDecoration && lineDecoration.clear()
        if(!props.selectLine){
            return
        }
        const decoration = editor!.createDecorationsCollection([{
            range: new monaco.Range(props.selectLine!, 0, props.selectLine!, 0),
            options: {
                isWholeLine: true,
                className: "line-decoration",
            },
        }])
        setLineDecoration(decoration)
    }
    useEffect(handleContentChange, [props.content])
    useEffect(handleSelectLineChange, [props.selectLine])
    return <div style={{width: "100%", height: "100%"}} ref={editorElement} className="code-editor"></div>
}