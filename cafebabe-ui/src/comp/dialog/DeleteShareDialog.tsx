import {ApiClient} from "../../api/ApiClient";
import {Button, Dialog, DialogBody, DialogFooter, FormGroup, InputGroup} from "@blueprintjs/core";
import React, {useState} from "react";
import {showToast} from "../Utils";

export interface DeleteShareDialogProps{
    isOpen?: boolean
    onClose?: () => void
    onDeleted?: () => void
}
const apiClient = ApiClient.getClient()
export function DeleteShareDialog(props: DeleteShareDialogProps){
    const [loading, setLoading] = useState(false)
    const [url, setUrl] = useState("")
    const [deletingToken, setDeletingToken] = useState("")
    const handleSubmit = async () => {
        try {
            setLoading(true)
            await apiClient.deleteShare(url, deletingToken)
            props.onClose?.()
            showToast("Share link deleted", "success")
        } catch (e) {
            setLoading(false)
            showToast(e + "", "danger")
        }
    }
    return <Dialog canEscapeKeyClose={false} canOutsideClickClose={false} onClose={props.onClose} isOpen={props.isOpen} title="Delete Share Link" className="bp4-dark">
        <DialogBody>
            <FormGroup
                label="Share link"
                labelFor="text-input"
            >
                <InputGroup onChange={e => setUrl(e.target.value)} id="share-link-input" />
            </FormGroup>
            <FormGroup
                label="Deleting token"
                labelFor="deleting-token-input"
            >
                <InputGroup onChange={e => setDeletingToken(e.target.value)} id="deleting-token-input" />
            </FormGroup>
        </DialogBody>
        <DialogFooter actions={
                <Button disabled={!url || !deletingToken} loading={loading} intent="primary" text="Delete" onClick={handleSubmit} />
            }
        />
    </Dialog>
}