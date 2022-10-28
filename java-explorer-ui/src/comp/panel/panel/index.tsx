export interface PanelContentProp {
    title: string | JSX.Element
    content?: JSX.Element
}

/**
 * A resizable panel
 * @constructor
 */
export function Panel(props: PanelContentProp){
    return <div className="pl-panel-content">
        <div className="pl-panel-title">{props.title}</div>
        <div className="pl-panel-body">{props.content}</div>
    </div>
}
