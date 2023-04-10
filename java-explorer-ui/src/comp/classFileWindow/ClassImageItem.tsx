import {ReactElement} from "react";

export interface ClassImageItemGroupRow{
    key: string
    value: string
}
export interface ClassImageItemGroup{
    groupName: string
    rows: ClassImageItemGroupRow[]
}
export interface ClassImageItemProps{
    title: string
    icon: ReactElement,
    itemGroups: ClassImageItemGroup[]
}
export const ClassImageItem = (props: ClassImageItemProps) => {
    const rowView = (row: ClassImageItemGroupRow) => <div className="class-image-item-group-row">
        <div className="class-image-item-group-row-key">
            {row.key}
        </div>
        <div className="class-image-item-group-row-value">
            {row.value}
        </div>
    </div>

    const groupView = (group: ClassImageItemGroup) => <div className="class-image-item-group">
        <div className="class-image-item-group-name">{group.groupName}</div>
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