import { Observable } from '@nativescript/core';
import { GeminiService } from '../services/gemini-service';
import { Clipboard } from '@nativescript/clipboard';

export class HomeViewModel extends Observable {
    private geminiService: GeminiService;
    private _isLoading: boolean = false;
    private _prompt: string = '';
    private _generatedText: string = '';

    constructor() {
        super();
        // Replace with your actual API key
        this.geminiService = new GeminiService('YOUR_GEMINI_API_KEY');
    }

    get isLoading(): boolean {
        return this._isLoading;
    }

    set isLoading(value: boolean) {
        if (this._isLoading !== value) {
            this._isLoading = value;
            this.notifyPropertyChange('isLoading', value);
        }
    }

    get prompt(): string {
        return this._prompt;
    }

    set prompt(value: string) {
        if (this._prompt !== value) {
            this._prompt = value;
            this.notifyPropertyChange('prompt', value);
        }
    }

    get generatedText(): string {
        return this._generatedText;
    }

    set generatedText(value: string) {
        if (this._generatedText !== value) {
            this._generatedText = value;
            this.notifyPropertyChange('generatedText', value);
        }
    }

    async generateText() {
        if (!this.prompt.trim()) {
            return;
        }

        this.isLoading = true;
        try {
            const text = await this.geminiService.generateText(this.prompt);
            this.generatedText = text;
        } catch (error) {
            console.error('Error:', error);
            this.generatedText = 'Error generating text. Please try again.';
        } finally {
            this.isLoading = false;
        }
    }

    copyToClipboard() {
        if (this.generatedText) {
            Clipboard.setText(this.generatedText);
        }
    }
}