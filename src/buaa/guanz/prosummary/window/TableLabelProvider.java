package buaa.guanz.prosummary.window;

import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		if (element instanceof List){
			List<String> arr = (List<String>)element;
            if(columnIndex == 0){
                return arr.get(0);
            }else if(columnIndex == 1){
                return arr.get(1);
            }
		}
		return "";
	}

}