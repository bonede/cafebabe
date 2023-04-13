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
    cpIndex?: number
    onSelectCpInfo?: (cpIndex?: number) => void
}

const RowView = (props: {row: ClassImageItemGroupRow}) => {
    const popoverContent = <ClassImageItem title={props.row.more?.groupName || ""} icon={<Icon icon="build" />} itemGroups={
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
        onMouseOver={props.row.onMouseOver}
        onMouseLeave={props.row.onMouseLeave}
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
    const rowView = (row: ClassImageItemGroupRow) => <RowView row={row} />
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