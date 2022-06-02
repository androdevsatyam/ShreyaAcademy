package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.shreya_scademy.app.DataBinderMapperImpl());
  }
}
