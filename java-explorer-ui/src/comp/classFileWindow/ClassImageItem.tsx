import {ReactNode} from "react";
import {Icon} from "@blueprintjs/core";
import {Popover2} from "@blueprintjs/popover2";

export interface ClassImageItemGroupRow{
    key: string
    value: string
    more?: ClassImageItemGroup
    flash?: boolean
    onMouseOver?: () => void
    onMouseLeave?: () => void,
    color?: string,
    cpIndices?: number[]
}
export interface ClassImageItemGroup{
    groupName?: string
    rows: ClassImageItemGroupRow[]
}
export interface ClassImageItemProps{
    title: string
    icon: ReactNode,
    itemGroups: ClassImageItemGroup[]
    onSelectLine?: (file: string, line: number) => void
    onSelectCpInfo?: (cpIndices?: number[]) => void
}

const RowView = (props: {row: ClassImageItemGroupRow, onSelectCpInfo?: (cpIndices?: number[]) => void}) => {
    const popoverContent = <ClassImageItem onSelectCpInfo={props.onSelectCpInfo} title={props.row.more?.groupName || ""} icon={<Icon icon="build" />} itemGroups={
        [
            {
                rows: props.row.more?.rows!
            }
        ]
    } />
    const popover = props.row.more ?
        <Popover2  content={popoverContent}>
            <Icon icon='info-sign' />
        </Popover2> : null
    return <div
        className={`class-image-item-group-row${props.row.flash ? " flash" : ""}`}
        onMouseOver={(e) => {
            e.stopPropagation()
            props.row.onMouseOver && props.row.onMouseOver()
            if(props.row.cpIndices && props.onSelectCpInfo) {
                props.onSelectCpInfo(props.row.cpIndices.filter(s => s !== undefined && s !== null))
            }
        }}
        onMouseLeave={(e) => {
            e.stopPropagation()
            props.row.onMouseLeave && props.row.onMouseLeave()
            props.onSelectCpInfo && props.onSelectCpInfo([])
        }}
    >

        <div className="class-image-item-group-row-key">
            {props.row.key}
        </div>
        <div className="class-image-item-group-row-value" style={{color: props.row.color ? props.row.color : undefined}}>
            {props.row.value} {popover}
        </div>
    </div>
}

export const ClassImageItem = (props: ClassImageItemProps) => {
    const rowView = (row: ClassImageItemGroupRow) => <RowView onSelectCpInfo={props.onSelectCpInfo} row={row} />
    const groupView = (group: ClassImageItemGroup) => <div className="class-image-item-group">
        {group.groupName && <div className="class-image-item-group-name">{group.groupName}</div>}
        <div className="class-image-item-group-rows">{group.rows.map(rowView)}</div>
    </div>

    return <div className="class-image-item">
        <div className="class-image-item-title">
            <div className="class-image-item-title-icon">{props.icon}</div>
            <div className="class-image-item-title-label">{props.title}</div>
        </div>
        <div className="class-image-item-groups">
            {props.itemGroups.map(groupView)}
        </div>
    </div>
}