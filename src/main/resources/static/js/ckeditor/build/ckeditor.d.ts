/**
 * @license Copyright (c) 2014-2024, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */
// @ts-ignore
import { ClassicEditor } from '@ckeditor/ckeditor5-editor-classic';
// @ts-ignore
import { CKFinderUploadAdapter } from '@ckeditor/ckeditor5-adapter-ckfinder';
// @ts-ignore
import { Autoformat } from '@ckeditor/ckeditor5-autoformat';
// @ts-ignore
import { Bold, Italic } from '@ckeditor/ckeditor5-basic-styles';
// @ts-ignore
import { BlockQuote } from '@ckeditor/ckeditor5-block-quote';
// @ts-ignore
import { CloudServices } from '@ckeditor/ckeditor5-cloud-services';
// @ts-ignore
import type { EditorConfig } from '@ckeditor/ckeditor5-core';
// @ts-ignore
import { Essentials } from '@ckeditor/ckeditor5-essentials';
// @ts-ignore
import { Heading } from '@ckeditor/ckeditor5-heading';
// @ts-ignore
import { Image, ImageCaption, ImageStyle, ImageToolbar, ImageUpload } from '@ckeditor/ckeditor5-image';
// @ts-ignore
import { Indent } from '@ckeditor/ckeditor5-indent';
// @ts-ignore
import { Link } from '@ckeditor/ckeditor5-link';
// @ts-ignore
import { List } from '@ckeditor/ckeditor5-list';
// @ts-ignore
import { MediaEmbed } from '@ckeditor/ckeditor5-media-embed';
// @ts-ignore
import { Paragraph } from '@ckeditor/ckeditor5-paragraph';
// @ts-ignore
import { PasteFromOffice } from '@ckeditor/ckeditor5-paste-from-office';
// @ts-ignore
import { Table, TableToolbar } from '@ckeditor/ckeditor5-table';
// @ts-ignore
import { TextTransformation } from '@ckeditor/ckeditor5-typing';
// @ts-ignore
import { Undo } from '@ckeditor/ckeditor5-undo';
declare class Editor extends ClassicEditor {
    static builtinPlugins: (typeof Autoformat | typeof BlockQuote | typeof Bold | typeof CKFinderUploadAdapter | typeof CloudServices | typeof Essentials | typeof Heading | typeof Image | typeof ImageCaption | typeof ImageStyle | typeof ImageToolbar | typeof ImageUpload | typeof Indent | typeof Italic | typeof Link | typeof List | typeof MediaEmbed | typeof Paragraph | typeof PasteFromOffice | typeof Table | typeof TableToolbar | typeof TextTransformation | typeof Undo)[];
    static defaultConfig: EditorConfig;
}
export default Editor;
