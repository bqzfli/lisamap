package srs.Layer;

import srs.Rendering.IRenderer;

/**图层渲染方式操作参数
 * @author Administrator
 *
 */
public class RendererArgs{
	IRenderer _renderer;

	/**构造函数
	 * @param renderer 渲染方式
	 */
	public RendererArgs(IRenderer renderer){
		_renderer = renderer;
	}

	/**渲染方式
	 * @return
	 */
	public IRenderer Renderer(){
		return _renderer; 
	}
	
	public void Renderer(IRenderer value){
		_renderer = value;     
	}
}